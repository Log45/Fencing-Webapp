export default function Timeline({ events, duration }: any) {
  return (
    <div
      className="relative h-2 w-full rounded-full bg-slate-700 overflow-hidden mt-4"
    >
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
  );
}