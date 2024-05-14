import { defineStore } from "pinia";

export const usePageDataStore = defineStore('pageData', {
  state: () => ({
    pages: new Map(),
  }),
  getters: {
  },
  actions: {
    setPageData(key: string, pageData: any) {
      return this.pages.set(key, pageData);
    },
    getPageData(key: string) {
      return this.pages.get(key);
    },
    deletePageData(key: string) {
      return this.pages.delete(key);
    }
  }
})
