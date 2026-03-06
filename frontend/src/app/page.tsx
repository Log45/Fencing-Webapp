import UploadVideo from "@/components/bouts/UploadVideo";

export default function Home() {
  return (
    <main className="min-h-screen flex items-center justify-center bg-slate-950 text-slate-100">
      <div className="max-w-xl w-full px-6 py-10 rounded-2xl bg-slate-900/70 shadow-2xl border border-slate-800">
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-semibold tracking-tight mb-2">
            Fencing Referee
          </h1>
          <p className="text-sm text-slate-300">
            Upload a bout video and let the AI score it for you.
          </p>
        </div>

        <UploadVideo />
      </div>
    </main>
  );
}