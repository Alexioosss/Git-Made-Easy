import { Monitor, Moon, Sun } from "lucide-react";
import { useTheme } from "next-themes";
import { useEffect, useState } from "react";

export default function ThemeToggle() {
    const { theme, setTheme, resolvedTheme } = useTheme();
    const [mounted, setMounted] = useState(false);

    useEffect(() => { setMounted(true); }, []);
    if(!mounted) { return null; }

    const changeTheme = () => {
        if(theme === "light") { setTheme("dark"); }
        else if(theme === "dark") { setTheme("system"); }
        else { setTheme("light"); }
    }

    const nextTheme = theme === "light" ? "dark" : theme === "dark" ? "system" : "light";
    const icon = theme === "system" ? <Monitor className="h-5 w-5" /> : resolvedTheme === "light" ? <Sun className="h-5 w-5" /> : <Moon className="h-5 w-5" />;

    return (
        <button onClick={changeTheme} title={`Switch to ${nextTheme} mode`} className="p-2 border rounded cursor-pointer">
            {icon}
        </button>
    );
}