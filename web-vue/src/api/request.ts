import axios from 'axios';
import type {AxiosInstance, AxiosResponse} from 'axios';
import {ElMessageBox} from "element-plus";

class ApiService {
    private axiosInstance: AxiosInstance;

    constructor() {
        this.axiosInstance = axios.create({
            baseURL: 'http://127.0.0.1:9090/catch-cash',
            timeout: 10000,
            withCredentials: false
        });

        // 添加 request 攔截器以添加 X-Access-Token 標頭
        this.axiosInstance.interceptors.request.use(config => {
            const token = localStorage.getItem('X-Access-Token'); // 假設從 localStorage 獲取 token
            if (token) {
                config.headers['X-Access-Token'] = token;
            }
            return config;
        });

        // 添加 response 攔截器
        this.axiosInstance.interceptors.response.use(
            this.handleSuccess,
            this.handleError
        );
    }

    private handleSuccess(response: AxiosResponse) {
        if(response.data.code === 50003){
            ElMessageBox.alert('Token 失效，請重新登入', '錯誤', {
                confirmButtonText: '確定',
                callback: () => {
                    location.replace("/")
                },
            }).then(r => location.replace("/"))
        }
        return response.data;
    }

    private handleError(error: any) {
        return Promise.reject(error);
    }

    public async get<T>(url: string, params?: any): Promise<T> {
        return this.axiosInstance.get(url, {params}).then(res => res.data);
    }

    public async post<T>(url: string, data: any): Promise<T> {
        return this.axiosInstance.post(url, data).then(res => res.data);
    }

    public async put<T>(url: string, data: any): Promise<T> {
        return this.axiosInstance.put(url, data).then(res => res.data);
    }

    public async delete<T>(url: string, params?: any): Promise<T> {
        return this.axiosInstance.delete(url, {params}).then(res => res.data);
    }
}

export default new ApiService();
