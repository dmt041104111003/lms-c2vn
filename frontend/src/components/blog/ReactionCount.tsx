"use client";

import { useState } from "react";
import { ReactionCountProps, REACTION_ICONS } from '~/constants/posts';

export default function ReactionCount({ reactions }: ReactionCountProps) {
  const [showDetails, setShowDetails] = useState(false);
  const totalReactions = Object.values(reactions).reduce((sum, count) => sum + count, 0);

  const topTypes = Object.entries(reactions)
    .filter(([type, count]) => count > 0 && REACTION_ICONS[type] !== undefined)
    .sort(([, a], [, b]) => b - a)
    .slice(0, 3);

  if (totalReactions === 0 || topTypes.length === 0) return null;

  return (
    <div className="relative">
      <div 
        className="flex items-center gap-4 text-lg text-gray-400 cursor-pointer hover:text-gray-300 transition-colors"
        onClick={() => setShowDetails(!showDetails)}
      >
        <div className="flex -space-x-2">
          {topTypes.map(([type], index) => (
            <div
              key={type}
              className="h-12 w-12 rounded-full bg-transparent border border-gray-700 flex items-center justify-center hover:scale-110 transition-transform"
              style={{ zIndex: topTypes.length - index }}
            >
              <span className="text-lg">{REACTION_ICONS[type]}</span>
            </div>
          ))}
        </div>
        <span className="text-gray-400 font-medium text-lg">
          {totalReactions} {totalReactions === 1 ? 'person' : 'people'} like this
        </span>
      </div>
    </div>
  );
} 