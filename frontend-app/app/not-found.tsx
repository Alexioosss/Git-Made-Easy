import { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
    title: "Page Not Found"
};

export default function NotFound() {
    return (
        <div className="flex flex-col items-center justify-center min-h-[calc(100vh-4rem)] sm:min-h-[calc(100vh-4.5rem)]">
            <h1 className="text-6xl sm:text-8xl font-bold text-center animate-[bounce_2s_infinite]
            after:content-[''] after:absolute after:left-1/2 after:-translate-x-1/2 after:bottom-0 after:w-[80%]
            after:h-[10px] after:bg-black-500 after:blur-[10px] after:opacity-100 after:bg-black dark:after:bg-white">404</h1>
            
            <p className="text-lg text-gray-700 dark:text-gray-200 mt-2 text-center">
                Oops... looks like the page you're looking for doesn't exist.
            </p>
            <Link href="/" title="Go to the home page"
            className="mt-6 px-6 py-3 bg-blue-700 text-white rounded-md hover:bg-blue-800 transition cursor-pointer transform hover:-translate-y-1 hover:scale-105">
                Go back to the home page
            </Link>
        </div>
    );
}