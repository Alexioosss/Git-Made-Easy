import { SiteHeader } from "@/components/site-header";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { AppConfig } from "@/config/AppConfig";
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

    const authGateway = AppConfig.instance.authGateway;

    async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
        e.preventDefault();
        setError("");
        setIsLoading(true);

        try {
            await authGateway.login(emailAddress, password);
            router.push("/dashboard");
        } catch(error: any) {
            setError(error.message || "Something went wrong");
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <div className="min-h-screen bg-background flex flex-col">
            <SiteHeader />
            <main className="flex-1 flex items-center justify-center px-4">
            <div className="w-full max-w-2xl bg-card p-14 rounded-xl shadow text-xl space-y-10">
                <div className="mb-8 text-center">
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
                    placeholder="you@example.com" required className="bg-secondary text-foreground h-14 text-xl px-4 placeholder:text-xl"
                    />
                </div>
    
                <div className="flex flex-col gap-2">
                    <Label htmlFor="password" className="text-foreground text-xl">
                    Password
                    </Label>
                    <Input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                    placeholder="Enter your password" required className="bg-secondary text-foreground h-14 text-xl px-4 placeholder:text-xl"
                    />
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
                <Link href="/register" className="text-primary transition-colors hover:text-primary/80 hover:underline">
                    Create one
                </Link>
                </p>
            </div>
            </main>
        </div>
    );
}