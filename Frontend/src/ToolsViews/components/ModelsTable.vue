<template>
  <div class="card">
    <!-- <div class="card-header pb-0">
      <h5>Models List</h5>
    </div> -->
    <div v-if="props.model.length != 0" class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0">
        <table class="table align-items-center mb-0">
          <thead>
            <tr>
              <th class=" text-secondary   opacity-7 text-center">index </th>
              <th class=" text-secondary   opacity-7 ">Model </th>
              <th class=" text-secondary   opacity-7 text-center">Type </th>
              <!-- <th class=" text-secondary   opacity-7 ">Strength</th> -->
              <th class=" text-secondary   opacity-7 text-center">Num of Params</th>
              <th class=" text-secondary   opacity-7 text-center">Num of Cons</th>
              <th class=" text-secondary   opacity-7 text-center">Num of TestSuites</th>
              <!-- <th class="text-center  text-secondary   opacity-7">Created time
              </th>
              <th class="text-center  text-secondary   opacity-7">Last Updated
                time</th> -->
              <th class="text-secondary opacity-7 text-center">Operation</th>
            </tr>
          </thead>
          <tbody>

            <tr class="cardTR" v-for="(model, index) in props.model">
              <td>
                <p class=" font-weight-bold mb-0 text-center">{{ index + 1 }}</p>
              </td>

              <td>
                <div class="d-flex flex-column justify-content-start align-items-start">
                  <div>
                    <h5 class="mb-0">{{ model.modelname }}</h5>
                  </div>
                  <div>
                    <p class="text-sm text-secondary mb-0 ">{{ model.modeldescriptions }}</p>
                  </div>
                </div>
              </td>
              <td>
                <p class=" font-weight-bold mb-0 text-center">{{ model.modeltype }}</p>
              </td>
              <!-- <td>
                <p v-if="model.modeltype != 'LLM'" class=" font-weight-bold mb-0 text-center">{{ model.strength }}
                </p>
                <p v-if="model.modeltype == 'LLM'" class=" font-weight-bold mb-0 text-center">\
                </p>
              </td> -->
              <td>
                <p class=" font-weight-bold mb-0 text-center">{{ model.NumofParams }}</p>
              </td>
              <td>
                <p class=" font-weight-bold mb-0 text-center">{{ model.NumofCons }}</p>
              </td>
              <td>
                <p v-if="model.modeltype != 'LLM'" class=" font-weight-bold mb-0 text-center">{{
                  model.NumOfTestSuites }}</p>
                <p v-if="model.modeltype == 'LLM'" class=" font-weight-bold mb-0 text-center">\</p>
              </td>

              <!-- <td class="align-middle text-center ">
                <p class=" font-weight-bold mb-0">{{ model.createdtimeFortmat }}</p>
              </td>
              <td class="align-middle text-center">
                <p class=" font-weight-bold mb-0">{{ model.lastupdatedtimeFortmat }}</p>
              </td> -->
              <td class="align-middle text-center">
                <div class="d-flex justify-content-center align-items-center">
                  <div>
                    <ArgonButton color="primary" variant="gradient" @click="EnterModels(model, index)">
                      <span class="fas fa-book-open text-white me-2" />
                      {{ model.modeltype == 'LLM' ? 'Enter Model' : 'Edit Model' }}
                    </ArgonButton>
                  </div>
                  <div style="margin-left: 10px;">
                    <ArgonButton color="success" variant="gradient" @click="EnterTestSuite(model, index)">
                      <span class="fas fa-print text-white me-2" />
                      TestSuites
                    </ArgonButton>
                  </div>
                  <div style="margin-left: 10px;">
                    <el-popconfirm width="300"
                      title="Are you sure to delete this model?  (All testsuites under this model will be deleted CASCADE)"
                      confirm-button-text="Yes" @confirm="confirmDelete(model)">
                      <template #reference>

                        <ArgonButton color="danger" variant="gradient">
                          <span class="far fa-trash-alt text-white me-2" />
                          Delete
                        </ArgonButton>
                      </template>
                    </el-popconfirm>
                  </div>
                </div>
              </td>
            </tr>


          </tbody>
        </table>
      </div>
    </div>



  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue';
import ArgonBadge from '../../CustomizedComponents/ArgonBadge.vue'
import ArgonButton from '../../CustomizedComponents/ArgonButton.vue';
import { useModelsStore } from '../../store/ToolsStore/modelsStore'
import { useCurrentModel } from '../../store/ToolsStore/currentModel'
import { useRoute, useRouter } from 'vue-router';
import { request } from '../../request';
import { ElNotification } from 'element-plus'
import { listAllModelsByUserID } from '../../ToolsViews/commonFunction.js'
import pinia from '../../store/store'
import { ElLoading } from 'element-plus'


const props = defineProps({
  model: Object,
})
const route = useRoute()
const router = useRouter()
const modelStore = useModelsStore(pinia)
const currentModel = useCurrentModel(pinia)

const EnterModels = (model, index) => {
  console.log("EnterModels", model)
  if (model.modeltype == 'LLM') {
    console.log("进入LLM")
    router.push({
      path: '/tools/LLMModelDetails',
      query:
      {
        modelid: model.modelid,
        
      }
    })
  }
  else {
    router.push({
      path: '/tools/modelsDetails',
      query:
      {
        modelid: model.modelid,
        index: index,
      }
    })
  }

}

const EnterTestSuite = (model, index) => {
  // 记录下当前的model
  router.push(
    {
      path: '/tools/TestSuiteDetails',
      query: {
        modelid: model.modelid,
        index: index
      }
    }
  )


}

const confirmDelete = async (model) => {
  // console.log("删除Model", model)
  let loadingInstance = ElLoading.service({ fullscreen: true })


  try {
    const DeleteModelRes = await request({
      url: '/tools/Delete',
      method: 'POST',
      data: {
        column: "model",
        modelid: model.modelid
      }
    })

    if (DeleteModelRes.DeleteStatus == 'success!') {
      // 实时更新页面数据
      await listAllModelsByUserID()
      ElNotification({
        title: 'Delete Success!',
        message: 'please check the results',
        type: 'success',
      })
      loadingInstance.close()


    }

    else {
      ElNotification({
        title: 'Delete Failed!',
        message: 'please check the results',
        type: 'error',
      })
      loadingInstance.close()
    }
  } catch (error) {
    console.log("error", error)
    ElNotification({
      title: 'Delete Failed!',
      message: 'please check the results',
      type: 'error',
    })
    loadingInstance.close()

  }

}




onMounted(() => {
  // console.log("props.model", props.model)
})
</script>

<style scoped></style>