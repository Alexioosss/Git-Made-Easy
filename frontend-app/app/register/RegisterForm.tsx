"use client"

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { GatewayFactory } from "@/config/GatewayFactory";
import { safeCallWrapper } from "@/lib/safeCallWrapper";
import { Eye, EyeOff, GitBranch, Loader2 } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function RegisterForm() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [emailAddress, setEmailAddress] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [isSuccess, setIsSuccess] = useState(false);

  const router = useRouter();
  const userGateway = GatewayFactory.instance.userGateway;
  
  async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    if(password != confirmPassword) { setError("Passwords do not match."); setIsLoading(false); return; }

    const response = await safeCallWrapper(() => userGateway.register(firstName, lastName, emailAddress, password));
    if(!response.ok) {
      setError(response.error || "Something went wrong");
      setIsLoading(false);
      return;
    }
    setIsLoading(false);
    setIsSuccess(true);
    setTimeout(() => router.push("/login"), 4000);
  }

  if(isSuccess) {
    return (
      <div className="min-h-[calc(100dvh-56px)] sm:min-h-[calc(100dvh-64px)] bg-background flex flex-col justify-center">
        <div className="flex justify-center px-4">
          <div className="w-full max-w-xl bg-card px-6 py-10 rounded-xl shadow text-center space-y-4 animate-fade-in">
            <div className="mx-auto flex h-20 w-20 items-center justify-center rounded-full bg-green-600">
              <svg className="h-12 w-12 text-white" fill="none" stroke="currentColor" strokeWidth="3" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <h2 className="text-3xl font-bold text-foreground">
              Account created successfully
            </h2>

            <p className="text-lg text-muted-foreground">
              Redirecting you to the login page...
            </p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-[calc(100dvh-56px)] sm:min-h-[calc(100dvh-64px)] bg-background flex flex-col justify-center">
      <div className="flex justify-center px-4 py-6">
        <div className="w-full max-w-xl bg-card px-6 pt-6 pb-4 rounded-xl shadow text-lg space-y-3">
          <div className="mb-6 text-center">
            <div className="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-xl bg-primary">
              <GitBranch className="h-10 w-10 text-primary-foreground" />
            </div>
            <h1 className="text-3xl font-bold text-foreground">
              Create a new account
            </h1>
            <p className="mt-2 text-lg text-muted-foreground">
              Save your progress and track your learning journey
            </p>
          </div>

          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div className="flex flex-col gap-2">
                <Label htmlFor="firstName" className="text-foreground text-xl">
                  First Name
                </Label>
                <Input id="firstName" value={firstName} onChange={(e) => setFirstName(e.target.value)}
                  placeholder="John" required className="bg-secondary text-foreground h-11 px-3 placeholder:text-xl !text-xl" />
              </div>
              <div className="flex flex-col gap-2">
                <Label htmlFor="lastName" className="text-foreground text-xl">
                  Last Name
                </Label>
                <Input id="lastName" value={lastName} onChange={(e) => setLastName(e.target.value)}
                  placeholder="Doe" required className="bg-secondary text-foreground h-11 px-3 placeholder:text-xl !text-xl" />
              </div>
            </div>

            <div className="flex flex-col gap-2">
              <Label htmlFor="email" className="text-foreground text-xl">
                Email Address
              </Label>
              <Input id="email" type="email" value={emailAddress} onChange={(e) => setEmailAddress(e.target.value)}
                placeholder="you@example.com" required className="bg-secondary text-foreground h-11 px-3 placeholder:text-xl !text-xl" />
            </div>

            <div className="flex flex-col gap-2">
              <Label htmlFor="password" className="text-foreground text-xl">
                Password
              </Label>
              <div className="relative">
                <Input id="password" type={showPassword ? "text" : "password"} value={password} onChange={(e) => setPassword(e.target.value)}
                placeholder="Create a password" required minLength={8} className="bg-secondary text-foreground h-11 px-3 placeholder:text-xl !text-xl" />
                
                <button type="button" className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                title={showPassword ? "Hide password" : "Show password"} onClick={() => setShowPassword(previousFlag => !previousFlag)}>
                  {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                </button>
              </div>
            </div>

            <div className="flex flex-col gap-2">
              <Label htmlFor="confirmPassword" className="text-foreground text-xl">
                Confirm Password
              </Label>
              <div className="relative">
                <Input id="confirmPassword" type={showPassword ? "text" : "password"} value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Re-enter your password" required minLength={8} className="bg-secondary text-foreground h-11 px-3 placeholder:text-xl !text-xl" />
                
                <button type="button" className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                title={showPassword ? "Hide password" : "Show password"} onClick={() => setShowPassword(previousFlag => !previousFlag)}>
                  {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                </button>
              </div>
            </div>

            {error && <p className="text-xl text-destructive">{error}</p>}

            <Button type="submit" className="mt-4 w-full h-16 text-xl hover:text-white transition-colors duration-400"
            disabled={isLoading} title={isLoading ? "Creating your account...": "Create Your Account"}>
              {isLoading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Creating account...
                </>
              ) : ( "Create Account" )}
            </Button>
          </form>

          <p className="mt-6 text-center text-xl text-muted-foreground">
            Already have an account?{" "}
            <Link href="/login" title="Sign in" className="text-primary transition-colors hover:text-primary/80 hover:underline">
              Sign in
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}