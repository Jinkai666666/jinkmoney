<template>
  <div style="padding: 20px;">
    <!-- 添加账单按钮 -->
    <el-button type="primary" @click="dialogVisible = true">添加账单</el-button>

    <!-- 账单表格 -->
    <el-table :data="records" border style="margin-top: 20px; width: 100%;">
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="category" label="分类" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="note" label="备注" />
      <el-table-column prop="date" label="日期" />
    </el-table>

    <!-- 添加账单弹窗 -->
    <el-dialog title="添加账单" v-model="dialogVisible" width="400px">
      <el-form :model="newRecord" label-width="80px">
        <el-form-item label="类型">
          <el-select v-model="newRecord.type" placeholder="请选择类型">
             <el-option label="收入" value="INCOME" />
             <el-option label="支出" value="EXPENSE" />
        </el-select>
        </el-form-item>

        <el-form-item label="分类">
          <el-input v-model="newRecord.category" placeholder="如：餐饮、交通" />
        </el-form-item>

        <el-form-item label="金额">
          <el-input type="number" v-model="newRecord.amount" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="newRecord.note" />
        </el-form-item>

        <el-form-item label="日期">
          <el-date-picker
            v-model="newRecord.date"
            type="date"
            placeholder="选择日期"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addRecord">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getRecords, addRecordApi } from '../api/record'

const records = ref([])
const dialogVisible = ref(false)
const newRecord = ref({
  type: '',      // 新增：类型（INCOME / EXPENSE）
  category: '',
  amount: '',
  note: '',
  date: ''
})

const loadRecords = async () => {
  try {
    const res = await getRecords()
    records.value = res
  } catch (e) {
    ElMessage.error('加载账单失败')
  }
}

const addRecord = async () => {
  if (!newRecord.value.type || !newRecord.value.category || !newRecord.value.amount || !newRecord.value.date) {
    ElMessage.error('请填写完整信息')
    return
  }

  try {
    await addRecordApi(newRecord.value)
    ElMessage.success('添加成功')
    dialogVisible.value = false
    await loadRecords()
  } catch (e) {
    ElMessage.error('添加失败')
  }
}

onMounted(loadRecords)
</script>
