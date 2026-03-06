export default function Timeline({ events, duration }: any) {
  return (
    <div className="mt-4 w-full">
      <div className="flex justify-between text-xs text-slate-300 mb-1">
        <span className="inline-flex items-center gap-1">
          <span className="w-2 h-2 rounded-full bg-emerald-500" />
          Left fencer scored
        </span>
        <span className="inline-flex items-center gap-1">
          <span className="w-2 h-2 rounded-full bg-orange-400" />
          Right fencer scored
        </span>
      </div>

      <div className="relative h-2 w-full rounded-full bg-slate-700 overflow-hidden">
        {duration > 0 &&
          events.map((e: any, idx: number) => {
            const left = (e.timestamp / duration) * 100;

            return (
              <div
                key={`${e.timestamp}-${idx}`}
                style={{
                  position: "absolute",
                  left: `${left}%`,
                  width: 4,
                  height: "100%",
                  background: e.side === "LEFT" ? "#22c55e" : "#f97316"
                }}
              />
            );
          })}
      </div>
    </div>
  );
}