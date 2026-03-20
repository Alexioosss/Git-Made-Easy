import * as React from 'react'

const MOBILE_BREAKPOINT = 768;

/**
 * Function to listen for window resize events, returning true if the user resized the window below the mobile breakpoint, false otherwise
 * @returns Returns a boolean value to represent if the screen is now in mobile view based on its viewport width
 */
export function useIsMobile() {
  const [isMobile, setIsMobile] = React.useState<boolean | undefined>(undefined);

  React.useEffect(() => {
    const mediaQueryList = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT - 1}px)`)
    const onChange = () => { setIsMobile(window.innerWidth < MOBILE_BREAKPOINT); }
    
    mediaQueryList.addEventListener('change', onChange);
    setIsMobile(window.innerWidth < MOBILE_BREAKPOINT);

    return () => mediaQueryList.removeEventListener('change', onChange);
  }, []);

  return !!isMobile;
}