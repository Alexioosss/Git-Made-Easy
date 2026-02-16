import { getApiClient } from "@/infrastructure/factories/singletonApiClient";
import { FetchAuthGateway } from "@/infrastructure/gateways/auth/FetchAuthGateway";
import { FetchUserGateway } from "@/infrastructure/gateways/users/FetchUserGateway";

export class Gateways {
    private static _instance: Gateways;

    private constructor() {}

    static get instance(): Gateways {
        if(!this._instance) { this._instance = new Gateways(); }
        return this._instance;
    }

    get apiClient() { return getApiClient(); }

    get userGateway() { return new FetchUserGateway(this.apiClient); }

    get authGateway() { return new FetchAuthGateway(this.apiClient); }
}