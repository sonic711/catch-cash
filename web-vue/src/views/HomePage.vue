<template>
  <div class="container">
    <el-form
        ref="ruleFormRef"
        :model="pageObj.form"
        :rules="rules">
      <el-row>
        <el-col :span="12">
          <el-form-item label="帳號" prop="account">
            <el-input
                v-model="pageObj.form.account"
                placeholder="請輸入帳號"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="密碼" prop="password">
            <el-input
                show-password
                prop="password"
                v-model="pageObj.form.password"
                placeholder="請輸入密碼"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-row>
      <el-col :span="24">
        <div class="button">
          <el-button @click="login" type="success">登入</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {usePageDataStore} from "@/stores/counter";
import {reactive, ref} from "vue";
import type {FormInstance, FormRules} from "element-plus";
import ApiService from "@/api/request";

const route = useRoute()
const router = useRouter()
const pageDataStore = usePageDataStore();
const pageObj = reactive({
  form: {
    account: 'ADMIN',
    password: 'ADMIN'
  },
})

const emit = defineEmits(['login-success'])

// 響應式物件
const ruleFormRef = ref<FormInstance>()

const rules = reactive<FormRules>({
  account: [{required: true, message: '請填寫帳號', trigger: ['blur', 'change']}],
  password: [{required: true, message: '請填寫密碼', trigger: 'change'}],
});

const login = async () => {
  ruleFormRef.value?.validate(async (valid: any) => {
    if (valid) {
      // todo: 登入動作
      const data = {
        "name": pageObj.form.account,
        "password": pageObj.form.password
      }
      const res = await ApiService.post('/sys/login', data) as any;
      // 儲存 access_token
      localStorage.setItem('X-Access-Token', res.accessToken)
      // 導向首頁
      await router.push('/about')
      emit('login-success')
    }
  })
}

</script>

<style scoped>
div.container {
  width: 800px;
  margin: 0 auto;
}
</style>
