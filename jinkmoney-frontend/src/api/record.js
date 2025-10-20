import request from './request'

// 获取账单列表
export const getRecords = () => request.get('/transaction/list')

// 添加账单
export const addRecordApi = data => request.post('/transaction/add', data)
