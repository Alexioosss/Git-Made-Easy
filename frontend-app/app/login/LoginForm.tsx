"use client"

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { GatewayFactory } from "@/config/GatewayFactory";
import { GitBranch, Loader2 } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function LoginForm() {
    const router = useRouter();

    const [emailAddress, setEmailAddress] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");

    const authGateway = GatewayFactory.instance.authGateway;

    async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
        e.preventDefault();
        setError("");
        setIsLoading(true);

        try {
            await authGateway.login(emailAddress, password);
            setTimeout(() => { router.push("/dashboard"); }, 3000);
        } catch(error: any) {
            setError(error.message || "Something went wrong");
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <div className="min-h-[calc(100dvh-56px)] sm:min-h-[calc(100dvh-64px)] bg-background flex flex-col justify-center">
            <div className="flex justify-center px-4">
                <div className="w-full max-w-xl bg-card px-6 pt-6 pb-4 rounded-xl shadow text-lg space-y-3">
                    <div className="mb-6 text-center">
                        <div className="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-xl bg-primary">
                            <GitBranch className="h-8 w-8 text-primary-foreground" />
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
                            <Input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password" required className="bg-secondary text-foreground h-11 text-lg px-3 placeholder:text-lg" />
                        </div>
            
                        {error && (
                            <p className="text-xl text-destructive">{error}</p>
                        )}
            
                        <Button type="submit" className="mt-4 w-full h-16 text-xl hover:text-white transition-colors duration-400" disabled={isLoading}>
                            {isLoading ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                Signing in...
                            </>
                            ) : (
                            "Sign In"
                            )}
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