import axios from 'axios';
import type {AxiosInstance, AxiosResponse} from 'axios';

class ApiService {
    private axiosInstance: AxiosInstance;

    constructor() {
        this.axiosInstance = axios.create({
            baseURL: 'http://127.0.0.1:9090/catch-cash',
            timeout: 10000,
            withCredentials: false
        });

        this.axiosInstance.interceptors.response.use(
            this.handleSuccess,
            this.handleError
        );
    }

    private handleSuccess(response: AxiosResponse) {
        return response;
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
