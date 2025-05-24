import {createRouter, createWebHashHistory} from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('../views/Dashboard.vue'),
      children: [
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
    }
  ]
})

// Navigation guard to check if user is logged in
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('X-Access-Token')

  if (to.path !== '/login' && !token) {
    // If trying to access a protected route without being logged in, redirect to login
    next('/login')
  } else if (to.path === '/login' && token) {
    // If trying to access login while already logged in, redirect to dashboard
    next('/dashboard')
  } else {
    // Otherwise proceed as normal
    next()
  }
})

export default router
