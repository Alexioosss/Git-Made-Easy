"use client"

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { GatewayFactory } from "@/config/GatewayFactory";
import { useAuth } from "@/context/AuthContext";
import { Eye, EyeOff, GitBranch, Loader2 } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function LoginForm() {
    const router = useRouter();
    const { refreshUser } = useAuth();
    const [emailAddress, setEmailAddress] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");
    const [isSuccess, setIsSuccess] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const authGateway = GatewayFactory.instance.authGateway;


    async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
        e.preventDefault();
        setError("");
        setIsLoading(true);

        try {
            await authGateway.login(emailAddress, password);
            setIsSuccess(true);
            await refreshUser();
            setTimeout(() => { router.push("/dashboard"); }, 2000);
        } catch(error: any) {
            if(error.code === "INVALID_CREDENTIALS") {
                setError(error.message);
            } else if(error.code === "EMAIL_NOT_VERIFIED") {
                setError(error.message + "\nIf the email address exists, verify your email address via the verification link.");
            } else if(error.code === "AUTH_REQUIRED") {
                setError("Your session has expired. Please log in again.");
            } else { setError(error.message || "Something went wrong"); }
        } finally {
            setIsLoading(false);
        }
    }

    if(isSuccess) {
        return (
            <div className="min-h-[calc(100dvh-56px)] sm:min-h-[calc(100dvh-64px)] bg-background flex justify-center items-center overflow-y-auto px-4 py-6">
                <div className="flex justify-center px-4">
                    <div className="w-full max-w-xl bg-card px-6 pt-6 pb-4 rounded-xl shadow text-lg space-y-3 animate-fade-in">
                        <div className="mx-auto flex h-20 w-20 items-center justify-center rounded-full bg-green-600">
                            <svg className="h-12 w-12 text-white" fill="none" stroke="currentColor" strokeWidth="3" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                            </svg>
                        </div>

                        <h2 className="text-3xl font-bold text-foreground">
                            Logged in successfully!
                        </h2>

                        <p className="text-lg text-muted-foreground">
                            Redirecting you to your dashboard...
                        </p>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-[calc(100dvh-56px)] sm:min-h-[calc(100dvh-64px)] bg-background flex flex-col justify-center">
            <div className="flex justify-center px-4">
                <div className="w-full max-w-xl bg-card px-6 pt-6 pb-4 rounded-xl shadow text-lg space-y-3">
                    <div className="mb-6 text-center">
                        <div className="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-xl bg-primary">
                            <GitBranch className="h-10 w-10 text-primary-foreground" />
                        </div>
                        <h1 className="text-3xl font-bold text-foreground">Welcome back!</h1>
                        <p className="mt-2 text-lg text-muted-foreground">
                            Sign in to continue tracking your progress
                        </p>
                    </div>
        
                    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                        <div className="flex flex-col gap-2">
                            <Label htmlFor="email" className="text-foreground text-xl">
                                Email
                            </Label>
                            <Input id="email" type="email" value={emailAddress} onChange={(e) => setEmailAddress(e.target.value)}
                            placeholder="you@example.com" required className="bg-secondary text-foreground h-11 text-lg px-3 placeholder:text-lg" />
                        </div>
            
                        <div className="flex flex-col gap-2">
                            <Label htmlFor="password" className="text-foreground text-xl">
                                Password
                            </Label>
                            <div className="relative">
                                <Input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                                placeholder="Enter your password" required className="bg-secondary text-foreground h-11 text-lg px-3 placeholder:text-lg" />

                                <button type="button" className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                                title={showPassword ? "Hide password" : "Show password"}
                                onMouseDown={() => setShowPassword(true)} onMouseUp={() => setShowPassword(false)} onMouseLeave={() => setShowPassword(false)}>
                                {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                                </button>
                            </div>
                        </div>
            
                        {error && (
                            <div className="animate-in fade-in slide-in-from-top-2 duration-1000 rounded-lg
                            border border-destructive/100 bg-destructive/20 p-3 text-xl text-destructive whitespace-pre-line">
                                {error}
                            </div>
                        )}
            
                        <Button type="submit" className="mt-4 w-full h-16 text-xl transition-colors duration-500" disabled={isLoading}>
                            {isLoading ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                Signing in...
                            </>
                            ) : ( "Sign In" )}
                        </Button>
                    </form>
        
                    <p className="mt-6 text-center text-xl text-muted-foreground">
                        {"Don't have an account? "}
                        <Link href="/register" title="Create an account" className="text-primary transition-colors hover:text-primary/80 hover:underline">
                            Create one
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}