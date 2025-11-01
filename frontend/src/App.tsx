import { useEffect, useState } from "react";

type Dino = {
  id: number;
  species: string;
  groupCode: string;
  lifespanYears?: number;
  lengthM?: number;
  speedKmh?: number;
  intelligence?: number;
  attack?: number;
  defense?: number;
};

export default function App() {
  const [data, setData] = useState<Dino[]>([]);
  useEffect(() => {
    fetch("http://localhost:8080/api/dinosaurs")
      .then((r) => r.json())
      .then(setData);
  }, []);
  return (
    <main style={{ padding: 16 }}>
      <h1>Dinosaur Data</h1>
      <table>
        <thead>
          <tr>
            <th>Group</th>
            <th>Species</th>
            <th>Length (m)</th>
            <th>Speed (km/h)</th>
            <th>Int</th>
            <th>Atk</th>
            <th>Def</th>
          </tr>
        </thead>
        <tbody>
          {data.map((d) => (
            <tr key={d.id}>
              <td>{d.groupCode}</td>
              <td>{d.species}</td>
              <td>{d.lengthM ?? "-"}</td>
              <td>{d.speedKmh ?? "-"}</td>
              <td>{d.intelligence ?? "-"}</td>
              <td>{d.attack ?? "-"}</td>
              <td>{d.defense ?? "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </main>
  );
}
