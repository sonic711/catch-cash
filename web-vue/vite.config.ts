import path from "path";
import type { ConfigEnv } from 'vite'
import { defineConfig, loadEnv } from 'vite'
import {createSvgIconsPlugin} from "vite-plugin-svg-icons";

import { convertEnv, getRootPath, getSrcPath } from './build-vite/utils'
import { createViteProxy, viteDefine } from './build-vite/config'
import { setupVitePlugins } from './build-vite/plugins'

// const pathResolve = (pathStr: string) => {
//   return path.resolve(__dirname, pathStr)
// }

export default defineConfig((configEnv: ConfigEnv) => {
  const srcPath = getSrcPath()
  const rootPath = getRootPath()
  const isBuild = configEnv.command === 'build'

  const viteEnv = convertEnv(loadEnv(configEnv.mode, process.cwd()))

  const { VITE_PORT, VITE_PUBLIC_PATH, VITE_USE_PROXY, VITE_PROXY_TYPE } = viteEnv
  return {
    base: VITE_PUBLIC_PATH,
    resolve: {
      alias: {
        '~': rootPath,
        '@': srcPath,
      },
    },
    define: viteDefine,
    plugins: [
      createSvgIconsPlugin({
        // 指定需要缓存的svg图标文件夹，即需要识别的svg都应该放在这个文件夹下
        iconDirs: [path.resolve(process.cwd(), 'src/assets/svg')],
        // iconDirs: [pathResolve('./src/assets')],
        // 指定symbolId格式（这里的配置与6.2步骤中的引入svg组件的name配置项写法有关）
        symbolId: 'icon-[dir]-[name]',
      }),
      setupVitePlugins(viteEnv, isBuild),
    ],
    server: {
      host: '0.0.0.0',
      port: VITE_PORT,
      open: true,
      proxy: createViteProxy(VITE_USE_PROXY, VITE_PROXY_TYPE as ProxyType),
    },
    build: {
      outDir: './dist/catch-cash',
      reportCompressedSize: false,
      sourcemap: false,
      chunkSizeWarningLimit: 1024, // chunk 大小警告的限制（单位kb）
      commonjsOptions: {
        ignoreTryCatch: false,
      },
      external: [
        '/pdf/build/pdf.worker.mjs'
      ]
    },
  }
})
