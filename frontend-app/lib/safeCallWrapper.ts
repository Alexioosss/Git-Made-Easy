export async function safeCallWrapper<T>(promise: Promise<T>) {
    try {
        const data = await promise;
        return { ok: true, data, error: null };
    } catch(error: any) {
        return {
            ok: false,
            data: null as T | null,
            error: error?.message ?? "Unknown error",
            status: error?.status ?? 0,
            code: error?.code ?? null
        };
    }
}