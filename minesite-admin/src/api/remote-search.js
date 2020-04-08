import request from '@/utils/request'

export function searchUser(name) {
  return request({
    url: '-dev/vue-element-admin/search/user',
    method: 'get',
    params: { name }
  })
}

export function transactionList(query) {
  return request({
    url: '-dev/vue-element-admin/transaction/list',
    method: 'get',
    params: query
  })
}
