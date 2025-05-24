import {createRouter, createWebHashHistory} from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomePage.vue')
    },
    {
      path: '',
      redirect: ''
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/firstPage',
      name: 'firstPage',
      component: () => import('../views/NextPage.vue')
    },
    {
      path: '/chatRoom',
      name: 'chatRoom',
      component: () => import('../views/ChatRoom.vue')
    },
  ]
})

export default router
