import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import TweetIgelFrontend from "./TweetIgelFrontend.jsx";

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <TweetIgelFrontend />
  </StrictMode>,
)
