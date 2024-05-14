<template>
  <el-dialog
      :model-value="prop.dialogVisible"
      :msg="prop.msg"
      @close="closeDialog"
      width="80%">
    <div class="greetings">
      <h1 class="green">{{ prop.msg }}</h1>
      <el-input
          v-model="pageObj.msg"
          style="width: 240px"
          placeholder="請輸入訊息"/>
    </div>
  </el-dialog>
</template>
<script setup lang="ts">
import {reactive} from "vue";

const pageObj = reactive({
  msg:'',
})

const prop = defineProps({
  dialogVisible: {type: Boolean, required: true},
  msg: {type: String, required: true},
});
const emit = defineEmits(["closeDialog"]);
const closeDialog = () => {
  emit("closeDialog", false, pageObj.msg);
};
</script>
<style scoped>
</style>
