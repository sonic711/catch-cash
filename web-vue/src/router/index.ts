import {createRouter, createWebHashHistory} from 'vue-router'

const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'notFound',
            component: () => import('../views/NotFound.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/login/LoginView.vue')
        },
        {
            path: '/dashboard',
            name: 'dashboard',
            component: () => import('../views/Dashboard.vue'),
            children: [
                {
                    path: '/home',
                    name: 'home',
                    component: () => import('../views/Home.vue')
                },
                {
                    path: '/about',
                    name: 'about',
                    component: () => import('../views/member/CreateMember1st.vue')
                },
                {
                    path: '/firstPage',
                    name: 'firstPage',
                    component: () => import('../views/member/CreateMember2rd.vue')
                },
                {
                    path: '/chatRoom',
                    name: 'chatRoom',
                    component: () => import('../views/chat/ChatRoom.vue')
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
