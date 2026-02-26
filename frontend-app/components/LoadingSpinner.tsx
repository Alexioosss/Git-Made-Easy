interface LoadingSpinnerProps {
    message: string;
};

export default function LoadingSpinner({ message }: LoadingSpinnerProps) {
    return (
        <div className="flex min-h-screen flex-col items-center justify-center bg-background">
            <div className="flex flex-col items-center">
                <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary border-t-transparent" />
                <p className="mt-5 text-center text-gray-700 dark:text-white text-lg md:text-3xl max-w-sm md:max-w-3xl mx-auto">
                    {message}
                </p>
            </div>
        </div>
    );
}