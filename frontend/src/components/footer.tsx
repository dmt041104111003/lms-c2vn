import Image from "next/image";
import Link from "next/link";
import { images } from "~/public/images";
import { ThemeToggle } from "./ui/theme-toggle";

export default function Footer() {
  return (
    <div className="relative z-30 border-t dark:border-white/20 bg-white/80 dark:bg-black/20  text-gray-900 dark:text-white">
      <footer className="relative mx-auto max-w-7xl px-6 py-16 lg:px-8">
        <div className="absolute left-6 right-6 top-0 h-px bg-gradient-to-r from-transparent via-white/20 to-transparent"></div>
        <div className="flex flex-col">
          <div className="grid w-full grid-cols-1 gap-12 sm:grid-cols-2 md:grid-cols-3">
            <div className="relative">
              <div className="absolute -top-2 left-0 h-1 w-8 bg-gradient-to-r from-blue-500 to-transparent opacity-60"></div>
              <h3 className="mb-6 text-lg font-bold uppercase tracking-wider text-gray-900 dark:text-white">Stay Connected with Cardano2VN</h3>
              <ul className="space-y-4">
                                 

              </ul>
            </div>
            <div className="relative">
              <div className="absolute -top-2 left-0 h-1 w-8 bg-gradient-to-r from-blue-500 to-transparent opacity-60"></div>
              <h3 className="mb-6 text-lg font-bold uppercase tracking-wider text-gray-900 dark:text-white">Follow Us</h3>
                             <ul className="space-y-4">
                 <li>
                                      <Link
                      className="group flex items-center text-gray-900 dark:text-white transition-colors duration-200 hover:text-blue-600 dark:hover:text-blue-400"
                      href="https://www.youtube.com/channel/UCJTdAQPGJntJet5v-nk9ebA"
                    >
                      <span className="mr-3 h-px w-2 bg-gray-600 transition-colors duration-200 group-hover:bg-blue-600 dark:group-hover:bg-blue-400"></span>YouTube
                    </Link>
                 </li>
                 <li>
                                      <Link
                      className="group flex items-center text-gray-900 dark:text-white transition-colors duration-200 hover:text-blue-600 dark:hover:text-blue-400"
                      href="https://t.me/cardano2vn"
                    >
                      <span className="mr-3 h-px w-2 bg-gray-600 transition-colors duration-200 group-hover:bg-blue-600 dark:group-hover:bg-blue-400"></span>Telegram
                    </Link>
                 </li>
                 <li>
                                      <Link
                      className="group flex items-center text-gray-900 dark:text-white transition-colors duration-200 hover:text-blue-600 dark:hover:text-blue-400"
                      href="https://github.com/cardano2vn"
                    >
                      <span className="mr-3 h-px w-2 bg-gray-600 transition-colors duration-200 group-hover:bg-blue-600 dark:group-hover:bg-blue-400"></span>GitHub
                    </Link>
                 </li>
               </ul>
            </div>
            <div className="relative">
              <div className="absolute -top-2 left-0 h-1 w-8 bg-gradient-to-r from-blue-500 to-transparent opacity-60"></div>
              <h3 className="mb-6 text-lg font-bold uppercase tracking-wider text-gray-900 dark:text-white">Company</h3>
                             <ul className="space-y-4">
                 
                                  
                   <li>
                                       <Link
                       className="group flex items-center text-gray-900 dark:text-white transition-colors duration-200 hover:text-blue-600 dark:hover:text-blue-400"
                       href="/privacy"
                     >
                       <span className="mr-3 h-px w-2 bg-gray-600 transition-colors duration-200 group-hover:bg-blue-600 dark:group-hover:bg-blue-400"></span>Privacy
                     </Link>
                   </li>
               </ul>
            </div>

          </div>
          <div className="mt-16 border-t border-white/10 pt-8">
            <div className="flex flex-col items-center justify-between md:flex-row">
              <div className="mb-4 flex items-center gap-4 md:mb-0">
                <Image className="h-8 w-auto opacity-80" src={images.logo} alt="cardano2vn" />
                <div className="text-sm text-gray-400">Trust Protocol for Distributed Work</div>
              </div>
              <div className="flex items-center gap-2 text-sm text-gray-400">
                <ThemeToggle />

                <span>|</span>
                <span>© 2025 Cardano2VN. All rights reserved.</span>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
