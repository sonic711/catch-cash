<template>
  <div class="s-canvas">
    <canvas
        id="s-canvas"
        :width="contentWidth"
        :height="contentHeight"
        @click="refreshCode"
    ></canvas>
  </div>
</template>
<script lang="ts" setup>
import { ref, onMounted, withDefaults, defineProps } from "vue";

// 定義props類型
interface CodeProps {
  /** 預設驗證碼 */
  defaultCode?: string;
  /** 容器寬度 */
  contentWidth?: number;
  /** 容器高度 */
  contentHeight?: number;
  /** 最大干擾線，0時無干擾線 */
  maxLine?: number;
  /** 最大干擾點，0時無干擾點 */
  maxDot?: number;
  /** 字體最小值 */
  fontSizeMin?: number;
  /** 字體最大值 */
  fontSizeMax?: number;
}
// props預設值
const props = withDefaults(defineProps<CodeProps>(), {
  contentWidth: 90,
  contentHeight: 30,
  fontSizeMin: 25,
  fontSizeMax: 30,
  maxLine: 4,
  maxDot: 10,
});

const emit = defineEmits(["update:verifyCode"]);
//驗證碼
const verifyCode = ref("");

onMounted(() => {
  verifyCode.value = props.defaultCode || makeCode();
  emit("update:verifyCode", verifyCode.value);
  drawPic(verifyCode.value);
});
// 產生校驗碼
const makeCode = (len = 4) => {
  let code = "";
  const codeLength = len; //驗證碼的長度
  // 定義產生驗證碼的字元集,去除易混淆的字元集1il0oO
  const identifyCodes = "123456789abcdefjhijkinpqrsduvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
  for (let i = 0; i < codeLength; i++) {
    // 獲取隨機字元
    code += identifyCodes[randomNum(0, identifyCodes.length)];
  }
  return code; //把code值賦給驗證碼
};

// 重置驗證碼
const refreshCode = () => {
  verifyCode.value = makeCode();
  emit("update:verifyCode", verifyCode.value);
  drawPic(verifyCode.value);
};
// 定義暴露介面
// defineExpose({ refreshCode });

//隨機數產生：根據角標拿字串的值
const randomNum = (min = 0, max: number) => Math.floor(Math.random() * (max - min)) + min;

// 產生一個隨機的顏色
function randomColor(min: number, max: number) {
  let r = randomNum(min, max);
  let g = randomNum(min, max);
  let b = randomNum(min, max);
  return "rgb(" + r + "," + g + "," + b + ")";
}
/** 繪製文字 */
function drawPic(verifyCode: string) {
  let canvas = document.getElementById("s-canvas") as HTMLCanvasElement;
  if (!canvas) {
    console.error("找不到 canvas 元素");
    return;
  }
  //建立一個2D物件作為上下文。
  let ctx = canvas.getContext("2d") as CanvasRenderingContext2D;
  ctx.textBaseline = "bottom";
  // 繪製背景
  ctx.fillStyle = "#e6ecfd";
  ctx.fillRect(0, 0, props.contentWidth, props.contentHeight);
  // 繪製文字
  for (let i = 0; i < verifyCode.length; i++) {
    drawText(ctx, verifyCode, i);
  }
  drawLine(ctx, props.maxLine);
  drawDot(ctx, props.maxDot);
}

/**在畫布上顯示資料
 * @param ctx CanvasRenderingContext2D
 * @param verifyCode 要顯示的文字
 * @param index 字元索引
 */
function drawText(ctx: CanvasRenderingContext2D, verifyCode: string, index: number) {
  ctx.fillStyle = randomColor(50, 160); // 隨機產生字體顏色
  ctx.font = randomNum(props.fontSizeMin, props.fontSizeMax) + "px SimHei"; // 隨機產生字體大小
  let x = (index + 1) * (props.contentWidth / (verifyCode.length + 1));
  let y = randomNum(props.fontSizeMax, props.contentHeight - 5);
  var deg = randomNum(-10, 15);
  // 修改座標原點和旋轉角度
  ctx.translate(x, y);
  ctx.rotate((deg * Math.PI) / 180);
  ctx.fillText(verifyCode[index], 0, 0);
  // 恢復座標原點和旋轉角度
  ctx.rotate((-deg * Math.PI) / 180);
  ctx.translate(-x, -y);
}
/** 繪製干擾線
 * @param ctx CanvasRenderingContext2D
 * @param max 最大干擾線個數
 */
function drawLine(ctx: CanvasRenderingContext2D, maxLine = 4) {
  if (maxLine <= 0) {
    return;
  }
  for (let i = 0; i < maxLine; i++) {
    ctx.strokeStyle = randomColor(150, 200);
    ctx.beginPath();
    ctx.moveTo(randomNum(0, props.contentWidth), randomNum(0, props.contentHeight));
    ctx.lineTo(randomNum(0, props.contentWidth), randomNum(0, props.contentHeight));
    ctx.stroke();
  }
}
/** 繪製干擾點
 * @param ctx CanvasRenderingContext2D
 * @param max 最大干擾點個數
 */
function drawDot(ctx: CanvasRenderingContext2D, maxDot = 10) {
  if (maxDot <= 0) {
    return;
  }
  for (let i = 0; i < maxDot; i++) {
    ctx.fillStyle = randomColor(0, 255);
    ctx.beginPath();
    ctx.arc(randomNum(0, props.contentWidth), randomNum(0, props.contentHeight), 1, 0, 2 * Math.PI);
    ctx.fill();
  }
}
</script>

