<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export interface ColumnDef {
  prop: string
  label: string
  width?: number | string
  formatter?: (row: any) => string
}

export interface OptionItem {
  label: string
  value: string | number | boolean
}

export interface FieldDef {
  prop: string
  label: string
  type?: 'input' | 'number' | 'select' | 'switch' | 'textarea' | 'date' | 'datetime' | 'time'
  options?: OptionItem[]
  required?: boolean
  placeholder?: string
  span?: number
}

interface CrudApi {
  page: (params: any) => Promise<any>
  create: (data: any) => Promise<any>
  update: (id: number, data: any) => Promise<any>
  remove: (id: number) => Promise<any>
}

const props = withDefaults(defineProps<{
  title: string
  en?: string
  api: CrudApi
  columns: ColumnDef[]
  search?: FieldDef[]
  form?: FieldDef[]
  rowKey?: string
  createText?: string
}>(), {
  en: '',
  search: () => [],
  form: () => [],
  rowKey: 'id',
  createText: '新增'
})

const loading = ref(false)
const submitLoading = ref(false)
const rows = ref<any[]>([])
const total = ref(0)
const query = reactive<any>({ pageNum: 1, pageSize: 10 })
const searchForm = reactive<any>({})
const dialogVisible = ref(false)
const editId = ref<number | null>(null)
const model = reactive<any>({})

function initSearch() {
  for (const field of props.search) searchForm[field.prop] = ''
}

function resetModel() {
  Object.keys(model).forEach((k) => delete model[k])
  for (const field of props.form) {
    model[field.prop] = field.type === 'switch' ? false : field.type === 'number' ? undefined : ''
  }
}

async function load() {
  loading.value = true
  try {
    const params: any = { pageNum: query.pageNum, pageSize: query.pageSize, ...searchForm }
    Object.keys(params).forEach((k) => {
      if (params[k] === '' || params[k] === null || params[k] === undefined) delete params[k]
    })
    const raw = await props.api.page(params)
    const body = raw?.data ?? raw
    rows.value = body?.records ?? (Array.isArray(body) ? body : [])
    total.value = Number(body?.total ?? rows.value.length)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editId.value = null
  resetModel()
  dialogVisible.value = true
}

function openEdit(row: any) {
  editId.value = Number(row[props.rowKey])
  resetModel()
  for (const field of props.form) {
    model[field.prop] = row[field.prop] ?? (field.type === 'switch' ? false : '')
  }
  dialogVisible.value = true
}

async function submit() {
  for (const field of props.form) {
    if (field.required && (model[field.prop] === '' || model[field.prop] === null || model[field.prop] === undefined)) {
      ElMessage.warning(`请填写${field.label}`)
      return
    }
  }
  if (submitLoading.value) return

  submitLoading.value = true
  try {
    if (editId.value === null) {
      await props.api.create({ ...model })
      ElMessage.success('创建成功')
    } else {
      await props.api.update(editId.value, { ...model })
      ElMessage.success('更新成功')
    }
    dialogVisible.value = false
    load()
  } catch {} finally {
    submitLoading.value = false
  }
}

async function remove(row: any) {
  await ElMessageBox.confirm('确定删除该记录吗？', '提示', { type: 'warning' })
  try {
    await props.api.remove(Number(row[props.rowKey]))
    ElMessage.success('删除成功')
    load()
  } catch {}
}

function search() {
  query.pageNum = 1
  load()
}

function reset() {
  initSearch()
  query.pageNum = 1
  load()
}

onMounted(() => {
  initSearch()
  resetModel()
  load()
})
</script>

<template>
  <section class="hs-page" :aria-label="title" :aria-busy="loading">


    <div class="hs-panel">
      <div class="hs-toolbar">
        <el-form inline class="hs-search" @submit.prevent="search">
          <div class="hs-search-fields">
            <el-form-item v-for="field in search" :key="field.prop" :label="field.label">
              <el-select v-if="field.type === 'select'" v-model="searchForm[field.prop]" clearable placeholder="全部" class="hs-search-control">
                <el-option v-for="opt in field.options" :key="String(opt.value)" :label="opt.label" :value="opt.value" />
              </el-select>
              <el-input v-else v-model="searchForm[field.prop]" clearable :placeholder="field.placeholder || '请输入'" class="hs-search-control" />
            </el-form-item>
          </div>
          <div class="hs-search-actions">
            <el-button native-type="submit" type="primary">查询</el-button>
            <el-button @click="reset">重置</el-button>
          </div>
        </el-form>
        <div class="hs-primary-actions">
          <el-button type="primary" @click="openCreate"><el-icon aria-hidden="true"><Plus /></el-icon><span>{{ createText }}</span></el-button>
        </div>
      </div>

      <div class="hs-table-wrap">
        <el-table :data="rows" v-loading="loading" border stripe>
          <el-table-column v-for="col in columns" :key="col.prop" :prop="col.prop" :label="col.label" :width="col.width" show-overflow-tooltip>
            <template #default="{ row }">
              {{ col.formatter ? col.formatter(row) : row[col.prop] }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="140" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
              <el-button link type="danger" @click="remove(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="hs-pagination">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="load"
          @size-change="search"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="editId === null ? `新增${title}` : `编辑${title}`" width="min(640px, calc(100vw - 32px))" append-to-body destroy-on-close>
      <el-form :model="model" label-width="110px" class="hs-dialog-form">
        <el-row :gutter="16">
          <el-col v-for="field in form" :key="field.prop" :span="field.span || 12">
            <el-form-item :label="field.label" :required="field.required">
              <el-select v-if="field.type === 'select'" v-model="model[field.prop]" clearable style="width: 100%">
                <el-option v-for="opt in field.options" :key="String(opt.value)" :label="opt.label" :value="opt.value" />
              </el-select>
              <el-switch v-else-if="field.type === 'switch'" v-model="model[field.prop]" />
              <el-input-number v-else-if="field.type === 'number'" v-model="model[field.prop]" style="width: 100%" />
              <el-date-picker v-else-if="field.type === 'date'" v-model="model[field.prop]" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
              <el-date-picker v-else-if="field.type === 'datetime'" v-model="model[field.prop]" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" />
              <el-input v-else-if="field.type === 'textarea'" v-model="model[field.prop]" type="textarea" :rows="3" />
              <el-input v-else v-model="model[field.prop]" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button :disabled="submitLoading" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" :disabled="submitLoading" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<style scoped>
.hs-page {
  min-width: 0;
}

.hs-panel {
  background: var(--bg-surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  padding: 20px;
}

.hs-toolbar {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  justify-content: space-between;
  margin-bottom: 18px;
}

.hs-search {
  display: flex;
  flex: 1;
  flex-wrap: wrap;
  gap: 8px 16px;
  min-width: 0;
  margin-bottom: 0;
}

.hs-search-fields,
.hs-search-actions,
.hs-primary-actions {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.hs-search-fields {
  flex: 1;
  min-width: 0;
}

.hs-search :deep(.el-form-item) {
  margin: 0;
}

.hs-search-control {
  width: 180px;
}

.hs-primary-actions {
  flex: 0 0 auto;
}

.hs-table-wrap {
  max-width: 100%;
  overflow-x: auto;
}

.hs-table-wrap :deep(.el-table) {
  min-width: 680px;
}

.hs-pagination {
  display: flex;
  justify-content: flex-end;
  max-width: 100%;
  margin-top: 16px;
  overflow-x: auto;
}

.hs-pagination :deep(.el-pagination) {
  flex-wrap: nowrap;
  min-width: max-content;
}

@media (max-width: 768px) {
  .hs-panel {
    padding: 16px;
  }

  .hs-toolbar {
    display: block;
  }

  .hs-search {
    display: block;
  }

  .hs-search-fields,
  .hs-search-actions {
    margin-bottom: 10px;
  }

  .hs-search-fields {
    display: grid;
    grid-template-columns: minmax(0, 1fr);
  }

  .hs-search :deep(.el-form-item),
  .hs-search-control {
    width: 100%;
  }

  .hs-primary-actions {
    justify-content: flex-start;
  }

  .hs-dialog-form :deep(.el-col) {
    flex: 0 0 100%;
    max-width: 100%;
  }
}
</style>