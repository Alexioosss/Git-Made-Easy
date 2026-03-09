export default function SuccessMessage({ title, message } : { title: string, message: string }) {
    return (
        <div className="min-h-[calc(100dvh-56px)] sm:min-h-[calc(100dvh-64px)] text-white dark:text-black flex justify-center items-center overflow-y-auto px-4 py-6">
            <div className="flex justify-center px-4">
                <div className="w-full max-w-xl bg-black text-white dark:bg-white dark:text-black px-6 pt-6 pb-4 rounded-xl shadow text-lg space-y-3 animate-fade-in">
                    <div className="mx-auto flex h-20 w-20 items-center justify-center rounded-full bg-green-600">
                        <svg className="h-12 w-12 text-white" fill="none" stroke="currentColor" strokeWidth="3" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                        </svg>
                    </div>

                    <h2 className="text-3xl font-bold text-center text-opacity/60">
                        {title}
                    </h2>

                    <p className="text-lg text-center text-opacity/60">
                        {message}
                    </p>
                </div>
            </div>
        </div>
    );
}