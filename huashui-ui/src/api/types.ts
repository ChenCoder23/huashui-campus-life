export interface Result<T = unknown> {
  code: number
  message: string
  data: T
}

export interface PageResult<T = unknown> {
  code?: number
  total: number
  page: number
  size: number
  records: T[]
}

export interface MybatisPage<T = unknown> {
  records: T[]
  total: number
  size: number
  current: number
}

export interface PageQuery {
  pageNum?: number
  pageSize?: number
}

export interface LoginVO {
  token: string
  userId: number
  username: string
  realName: string
  userType: string
  avatar?: string
}

export interface MenuItem {
  id: number
  parentId: number
  menuName: string
  menuType: 'DIRECTORY' | 'MENU' | 'BUTTON' | string
  path: string
  component?: string
  icon?: string
  permission?: string
  sortOrder?: number
  isHome?: number
  hidden?: number
  status?: number
  children?: MenuItem[]
}