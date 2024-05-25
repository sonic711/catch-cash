<template>
  <div class="container">
    <div class="button">
      <el-row>
        <el-col :span="12">
          <el-button @click="getMembers" type="success">取得上一頁得到的資料</el-button>
          <el-button @click="router.go(-1)">回到上一頁</el-button>
          <el-button @click="openDialog">popup</el-button>
        </el-col>
        <el-col :span="12">
          <el-input
              v-model="pageObj.msg"
              style="width: 240px"
              placeholder="請輸入訊息"/>
          <!--          <p class="errorMsg">{{ pageObj.msg }}</p>-->
        </el-col>
      </el-row>
      <HelloWorld
          :dialog-visible="pageObj.dialogVisible"
          :msg="pageObj.msg"
          @closeDialog="closeDialog"/>
    </div>
    <br>
    <el-form
        ref="ruleFormRef"
        :model="pageObj.form"
        label-width="30%">
      <el-row>
        <el-col :span="12">
          <el-form-item label="帳號" prop="account">
            <el-input
                v-model="pageObj.form.account"
                style="width: 240px"
                placeholder="請輸入帳號"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <div class="button">
            <el-button @click="query" type="success">查詢</el-button>
            <el-button @click="queryFromDB" type="success">從DB查詢</el-button>
          </div>
        </el-col>
      </el-row>
    </el-form>
    <div class="query-result">
      <div v-if="hasData">
        <el-table v-loading="loading" :data="pageObj.members" border stripe>
          <el-table-column label="帳號" prop="account"/>
          <el-table-column label="密碼" prop="pwd"/>
        </el-table>
      </div>
      <div v-else>
        <p class="errorMsg">查無資料</p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">

import {usePageDataStore} from "@/stores/counter";
import {onBeforeMount, onBeforeUpdate, onMounted, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {ElMessageBox} from "element-plus";
import axios from 'axios';
import HelloWorld from "@/components/Popup.vue";

const route = useRoute()
const router = useRouter()
const pageDataStore = usePageDataStore();
const pageObj = reactive({
  form: {
    account: ''
  },
  members: [] as any[],
  msg: 'Hello world',
  dialogVisible: false
})
const hasData = ref(false)
const loading = ref(false)


const openDialog = () => {
  pageObj.dialogVisible = true
};
const closeDialog = (bool: boolean, msg: string) => {
  pageObj.dialogVisible = bool;
  pageObj.msg = msg
};

const getMembers = () => {
  const members = pageDataStore.getPageData('members');
  if (members.length > 0) {
    hasData.value = true
    pageObj.members = members
  }
}
const query = async () => {
  pageObj.members.some(item => {
    if (item.account === pageObj.form.account) {
      ElMessageBox.alert(item.message, {
        confirmButtonText: '確定',
        callback: () => {

        },
      })
    }
  })
}
const queryFromDB = async() => {
  loading.value = true
  await axios.get('http://localhost:441/sslDemo/server/get').then(res => {
    if (res.status === 200) {
      console.log(res.data)
      pageObj.members = res.data
    } else {
      console.log('error')
    }
  });
  // 接著執行
  loading.value = false
}

onBeforeMount(() => {

})
onMounted(() => {
  console.log('before to next page:' + route.query.type)
  // const members = pageDataStore.getPageData('members');
  // if (members.length > 0) {
  //   hasData.value = true
  //   pageObj.members = members
  // }
})
onBeforeUpdate(() => {
  // console.log('before to next page:' + route.query.type)
})

</script>

<style scoped>
p.errorMsg {
  color: red;
  text-align: center;
  font-size: 18px;
}

div.container {
  width: 800px;
  margin: 0 auto;
}
</style>
