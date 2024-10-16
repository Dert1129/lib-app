import './index.scss';
import {Route, Routes, BrowserRouter} from "react-router-dom";
import Catalog from './components/Catalog';
import { createBrowserHistory } from 'history';
const history = createBrowserHistory();
function App() {
  return (
    <div className="App">
      <BrowserRouter history={history}>
        <Routes>
            <Route path="/" element={<Catalog />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
