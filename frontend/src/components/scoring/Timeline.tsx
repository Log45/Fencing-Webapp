export default function Timeline({ events, duration }: any) {
    return (
      <div style={{ position: "relative", height: 10, background: "#ccc" }}>
        {events.map((e: any) => {
          const left = (e.timestamp / duration) * 100;
  
          return (
            <div
              key={e.id}
              style={{
                position: "absolute",
                left: `${left}%`,
                width: 4,
                height: 10,
                background: "red"
              }}
            />
          );
        })}
      </div>
    );
  }