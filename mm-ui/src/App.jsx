import React, { useEffect, useState } from 'react'

const API = (path, opts={}) => fetch(`http://localhost:8080/api${path}`, {
  headers: { 'Content-Type': 'application/json' },
  ...opts
}).then(r => r.json?.() ?? r)

export default function App() {
  const [state, setState] = useState(null)
  const [actions, setActions] = useState([])
  const [busy, setBusy] = useState(false)

  const load = async () => {
    const [s, a] = await Promise.all([API('/state'), API('/actions')])
    setState(s); setActions(a)
  }
  useEffect(() => { load() }, [])

  const act = async (cityIndex, ruleId) => {
    setBusy(true)
    const s = await API('/act', { method:'POST', body: JSON.stringify({ cityIndex, ruleId }) })
    setState(s)
    setBusy(false)
  }
  const undo = async () => { setBusy(true); setState(await API('/undo', { method:'POST' })); setBusy(false) }
  const redo = async () => { setBusy(true); setState(await API('/redo', { method:'POST' })); setBusy(false) }
  const autoplay = async (turns=5) => { setBusy(true); setState(await API('/autoplay', { method:'POST', body: JSON.stringify({ turns }) })); setBusy(false) }
  const reset = async () => { setBusy(true); await fetch('http://localhost:8080/api/reset', { method:'POST' }); await load(); setBusy(false) }

  if (!state) return <p>Kraunama...</p>

  const outcome = state.outcome
  const finished = outcome !== 'IN_PROGRESS'

  return (
    <div style={{ fontFamily:'Inter, system-ui, Arial', maxWidth: 980, margin:'24px auto', padding:'0 16px' }}>
      <header style={{ display:'flex', alignItems:'center', justifyContent:'space-between' }}>
        <h1>MiniCity</h1>
        <div style={{ display:'flex', gap:8 }}>
          <button onClick={undo} disabled={busy}>Undo</button>
          <button onClick={redo} disabled={busy}>Redo</button>
          <button onClick={()=>autoplay(5)} disabled={busy}>Autoplay x5</button>
          <button onClick={reset} disabled={busy}>Reset</button>
        </div>
      </header>

      <p>Turų skaičius: <b>{state.turn}</b> | Būsena: <b>{state.outcome}</b></p>

      <div style={{ display:'grid', gridTemplateColumns:'1fr 1fr', gap:16 }}>
        {state.cities.map((c, idx) => (
          <div key={idx} style={{ border:'1px solid #ddd', borderRadius:12, padding:16 }}>
            <h3 style={{ marginTop:0 }}>{c.name}</h3>
            <p>Gyventojai: {c.population.toLocaleString('lt-LT')}</p>
            <p>Biudžetas: {c.budget}</p>
            <p>Laimė: {c.indicators.happiness} | Saugumas: {c.indicators.safety} | Aplinka: {c.indicators.environment}</p>
            <div style={{ display:'flex', flexWrap:'wrap', gap:8 }}>
              {actions.map(a => (
                <button key={a.id} disabled={busy || finished} onClick={()=>act(idx, a.id)} title={a.description}>
                  {a.name}
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>

      <section style={{ marginTop:24 }}>
        <h3>Žurnalas</h3>
        <div style={{ border:'1px solid #eee', padding:12, borderRadius:8, background:'#fafafa' }}>
          {(state.log ?? []).slice().reverse().map((line, i) => <div key={i}>{line}</div>)}
        </div>
      </section>
    </div>
  )
}
