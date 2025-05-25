<template>
  <div class="container">
    <el-form
        ref="ruleFormRef"
        :model="pageObj.form"
        :rules="rules"
        label-width="30%">
      <el-row>
        <el-col :span="12">
          <el-form-item label="帳號" prop="name">
            <el-input
                v-model="pageObj.form.name"
                style="width: 240px"
                placeholder="請輸入帳號"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="密碼" prop="password">
            <el-input
                prop="password"
                v-model="pageObj.form.password"
                style="width: 240px"
                placeholder="請輸入密碼"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="性別" prop="sex">
            <el-radio-group v-model="pageObj.form.sex">
              <el-radio :label="'B'">男</el-radio>
              <el-radio :label="'G'">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="興趣" prop="like">
            <el-select
                v-model="pageObj.form.like"
                placeholder="請選擇"
                clearable
            >
              <el-option
                  v-for="item in likeOptions"
                  :key="item.value"
                  :value="item.value"
                  :label="item.value+' '+item.label"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="圖片" prop="img">
            <input type="file" @change="onFileChange"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-row>
      <el-col :span="24">
        <div class="button">
          <el-button @click="submitForm" type="success">送出</el-button>
          <el-button @click="clearForm" type="">清除</el-button>
          <el-button @click="queryMembers" type="success">取得所有資料</el-button>
          <el-button @click="toNextPage" type="success">toNextPage</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div v-if="imageUrl">
          <h2>預覽:</h2>
          <img :src="imageUrl" alt="Image Preview" style="max-width: 300px;"/>
        </div>
      </el-col>
    </el-row>
  </div>
  <div class="query-result">
    <div v-if="hasData">
      <el-table v-loading="loading" :data="pageObj.members" border stripe>
        <el-table-column label="帳號" prop="name"/>
        <el-table-column label="密碼" prop="password"/>
        <el-table-column label="操作" align="left" width="90">
          <template #default="scope">
            <el-button link type="primary" @click="showImg(scope.row, scope.$index)">
              顯示圖片
            </el-button>
            <el-button link type="primary" @click="remove(scope.row, scope.$index)">
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-else style="display: none">
      <p>查無資料</p>
    </div>
    <el-image-viewer
        v-if="showImagePreview"
        :zoom-rate="1.2"
        @close="closePreview"
        :url-list="imgPreviewList"
    />
  </div>
</template>


<script setup lang="ts">
import {useRoute, useRouter} from 'vue-router'
import {onMounted, reactive, ref} from "vue";
import {ElMessage, ElMessageBox, type FormInstance, type FormRules, type UploadProps} from "element-plus";
import {usePageDataStore} from "@/stores/counter";
import axios from "axios";
import ApiService from '../../api/request';

const imgPreviewList = ref<any>([])
const showImagePreview = ref(false)
const currentBase64FileData = reactive({
  base64: '',
  name: ''
})

// route
const route = useRoute()
const router = useRouter()
// 使用PageData
const pageDataStore = usePageDataStore()
const pageObj = reactive({
  form: {
    name: "",
    password: "",
    message: "",
    sex: "G",
    file: {} as any,
  } as any,
  members: [] as any[],
})
// 響應式物件
const ruleFormRef = ref<FormInstance>()

const rules = reactive<FormRules>({
  name: [{required: true, message: '請填寫帳號', trigger: ['blur', 'change']}],
  password: [{required: true, message: '請填寫密碼', trigger: 'change'}],
});
const hasData = ref(true)
const loading = ref(false)
const likeOptions = ref([
      {value: '1', label: '節目'},
      {value: '2', label: '電影'},
    ]
)
const queryMembers = async () => {
  loading.value = true
  try {
    const res = await ApiService.get('/api/member') as any;
    pageObj.members = res as any[]
  } catch (error) {
    console.error('Error:', error);
  }
  loading.value = false

  // await axios.get('http://127.0.0.1:9090/catch-cash/api/member').then(res => {
  //   console.log(res)
  //   if(res.status === 200){
  //     console.log(res.data)
  //     pageObj.members = res.data as any[]
  //   }
  //   loading.value = false
  // });

  // 模擬資料
  // mockData()
  //     .then(data => {
  //       console.log(data)
  //       pageObj.members = data as any[]
  //       hasData.value = pageObj.members.length > 0;
  //       loading.value = false
  //     })
  //     .catch(error => {
  //       console.error(error); // 在這裡處理錯誤
  //     });
}
const mockData = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([{
        name: "John",
        password: "123456",
        message: "Welcome John!"
      }, {
        name: "Mary",
        password: "654321",
        message: "Welcome Mary!"
      }, {
        name: "Tom",
        password: "111111",
        message: "Welcome Tom!"
      }]);
    }, 1000);
  });
};
const submitForm = () => {
  ruleFormRef.value?.validate(async (valid: any) => {
    if (valid) {
      // pageObj.members.push(pageObj.form)
      const data = {
        name: pageObj.form.name,
        password: pageObj.form.password,
        profileImage: pageObj.form.img,
      }
      console.log(data)
      await ApiService.post('/api/member', data).then(res => {
        queryMembers()
      });
    } else {
      await ElMessageBox.alert('這裡發生了錯誤', '錯誤', {
        confirmButtonText: '確定',
        callback: () => {
          ElMessage({
            type: 'warning',
            message: `剛剛似乎發生了點錯誤，請再次確認並重新填寫。`,
          })
        },
      })
    }
  })
}
const toNextPage = () => {
  // console.log('before to next page:' + route.query.type)
  pageDataStore.setPageData('members', pageObj.members)
  router.push({
    name: 'firstPage',
    query: {
      type: '123',
    }
  });
}

const clearForm = () => {
  ruleFormRef.value?.resetFields()
  pageObj.members = []
}

const showImg = async (row: any, index: number) => {
  try {
    let res = await ApiService.get(`/api/member/image/${row.id}`) as any;
    currentBase64FileData.base64 = res
    showImagePreview.value = true
    imgPreviewList.value = [currentBase64FileData.base64]
  } catch (error) {
    console.error('Error:', error);
  }
  loading.value = false
}

const closePreview = () => {
  imgPreviewList.value = []
  showImagePreview.value = false
}

const imageUrl = ref('')

const onFileChange = (e: any) => {
  const file = e.target.files[0]
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    pageObj.form.img = reader.result as string
    imageUrl.value = reader.result as string
  }
}

const remove = async (row: any, index: number) => {
  // pageObj.members.splice(index, 1)
  await ApiService.delete(`/api/member/${row.id}`).then(res => {
    queryMembers()
  });
}
onMounted(() => {
  queryMembers()
})

</script>
<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

div.container {
  width: 800px;
  margin: 0 auto;
}
</style>
