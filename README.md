# AIONOS Customer-Facing Resolution Agent

A Spring Boot and React prototype for the AIONOS airline-disruption assignment. It keeps policy decisions deterministic in Java and uses H2 for local demo data.

## Requirements

- Java 21 (the Maven build uses `--release 21`)
- Maven 3.9+
- Node.js 20+

## Run locally

Open two terminals from the project root.

```powershell
cd backend
mvn spring-boot:run
```

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The backend runs at `http://localhost:8080`; the H2 console is available at `http://localhost:8080/h2-console`.

## Verification

```powershell
cd backend
mvn test

cd ..\frontend
npm run build
```

## Assignment scenarios

1. Priya Nair / `SK4821X`: airline-caused cancellation; refund is initiated to the original payment method and a complimentary business-class upgrade is escalated.
2. Arvind Kulkarni / `TR1190B`: four-hour delay; a ₹500 meal voucher and lounge access are issued, while hotel accommodation is blocked.
3. Meher Kaur / `WL7742`: six-hour delay; delayed-hours hotel accommodation is arranged, while a full-night extension and ₹2,000 fare-difference waiver are escalated.

The assignment facts, assumptions, and AI-tool disclosure are in `docs/ASSUMPTIONS.md` and `docs/AI_TOOLS.md`.