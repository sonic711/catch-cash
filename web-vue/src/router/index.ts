import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomePage.vue')
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
