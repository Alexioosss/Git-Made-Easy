"use client"

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { GatewayFactory } from "@/config/GatewayFactory";
import { useAuth } from "@/context/AuthContext";
import { safeCallWrapper } from "@/lib/safeCallWrapper";
import { Eye, EyeOff, GitBranch, Loader2 } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function LoginForm() {
    const router = useRouter();
    const { refreshUser } = useAuth();
    const [emailAddress, setEmailAddress] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>("");
    const [isSuccess, setIsSuccess] = useState<boolean>(false);
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [needsEmailVerification, setNeedsEmailVerification] = useState<boolean>(false);
    const [secondsBeforeResending, setSecondsBeforeResending] = useState<number>(0);
    const authGateway = GatewayFactory.instance.authGateway;


    async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
        e.preventDefault();
        setError("");
        setIsLoading(true);
        const response = await safeCallWrapper(() => authGateway.login(emailAddress, password));
        if(!response.ok) {
            if(response.code === "INVALID_CREDENTIALS") { setError(response.error); }
            else if(response.code === "EMAIL_NOT_VERIFIED") {
                setError(response.error + "\nIf the email address exists, verify your email address via the verification link.");
                setNeedsEmailVerification(true);
            }
            else if(response.code === "NETWORK_ERROR") { setError(response.error); }
            else { setError(response.error || "Something went wrong"); }
            setIsLoading(false);
            return;
        }
        setIsLoading(false);
        setIsSuccess(true);
        await new Promise(resolve => setTimeout(resolve, 50));
        await refreshUser();
        setTimeout(() => { router.push("/dashboard"); }, 1500);
    }

    async function handleResendVerificationEmail() {
        if(secondsBeforeResending > 0) { return; }
        const response = await safeCallWrapper(() => authGateway.resendVerificationEmail(emailAddress));
        if(!response.ok) { setError(response.error || "Could not resend the verification email."); return; }
        setError("A new verification email has been sent.");
        setSecondsBeforeResending(60);
        const interval = setInterval(() => {
            setSecondsBeforeResending(previous => {
                if(previous <= 1) { clearInterval(interval); return 0; }
                return previous - 1;
            });
        }, 1000);
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
                                <Input id="password" type={showPassword ? "text" : "password"} value={password} onChange={(e) => setPassword(e.target.value)}
                                placeholder="Enter your password" required className="bg-secondary text-foreground h-11 text-lg px-3 placeholder:text-lg" />

                                <button type="button" className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                                title={showPassword ? "Hide password" : "Show password"}
                                onClick={() => setShowPassword(!showPassword)}>
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

                    {needsEmailVerification && secondsBeforeResending === 0 && (
                        <Button type="button" title="Resend the verification email to your email address"
                        variant="outline" className="w-full h-12 text-lg mt-2" onClick={handleResendVerificationEmail}>
                            Resend verification email
                        </Button>
                    )}

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