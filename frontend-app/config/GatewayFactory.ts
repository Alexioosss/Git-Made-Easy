import { getApiClient } from "@/infrastructure/factories/singletonApiClient";
import { FetchAuthGateway } from "@/infrastructure/gateways/auth/FetchAuthGateway";
import { FetchLessonProgressGateway } from "@/infrastructure/gateways/lessonProgress/FetchLessonProgressGateway";
import { FetchLessonGateway } from "@/infrastructure/gateways/lessons/FetchLessonGateway";
import { FetchTaskProgressGateway } from "@/infrastructure/gateways/taskProgress/FetchTaskProgressGateway";
import { FetchTaskGateway } from "@/infrastructure/gateways/tasks/FetchTaskGateway";
import { FetchUserGateway } from "@/infrastructure/gateways/users/FetchUserGateway";

export class GatewayFactory {
    private static _instance: GatewayFactory;

    // Create readonly gateway instances so they are made only instantiated once
    private readonly _apiClient = getApiClient();
    private readonly _userGateway = new FetchUserGateway(this._apiClient);
    private readonly _authGateway = new FetchAuthGateway(this._apiClient);
    private readonly _lessonGateway = new FetchLessonGateway(this._apiClient);
    private readonly _taskGateway = new FetchTaskGateway(this._apiClient);
    private readonly _taskProgressGateway = new FetchTaskProgressGateway(this._apiClient);
    private readonly _lessonProgressGateway = new FetchLessonProgressGateway(this._apiClient);

    private constructor() {}

    static get instance(): GatewayFactory { return this._instance ??= new GatewayFactory(); }

    get apiClient() { return getApiClient(); }

    get userGateway() { return this._userGateway; }

    get authGateway() { return this._authGateway; }

    get lessonGateway() { return this._lessonGateway; }

    get taskGateway() { return this._taskGateway; }

    get taskProgressGateway() { return this._taskProgressGateway; }

    get lessonProgressGateway() { return this._lessonProgressGateway; }
}