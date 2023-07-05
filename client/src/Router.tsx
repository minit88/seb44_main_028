import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainPage from './pages/Main/views/MainPage';
import MyPage from './pages/MyPage/views/MyPage';
import LoginPage from './pages/Login/views/LoginPage';
import ItemListPage from './pages/ItemList/views/ItemListPage';
import DetailPage from './pages/Detail/views/DetailPage';
import CreatePage from './pages/Create/views/CreatePage';
import ReservationPage from './pages/Reservation/views/ReservationPage';
import UpdatePage from './pages/Update/views/UpdatePage';
import ChattingPage from './pages/Chatting/views/ChattingPage';

function Router() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/itemlist" element={<ItemListPage />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/detail/:id" element={<DetailPage />} />
        <Route path="/create" element={<CreatePage />} />
        <Route path="/update/:id" element={<UpdatePage />} />
        <Route path="/reservation" element={<ReservationPage />} />
        <Route path="/chatting" element={<ChattingPage />} />
      </Routes>
    </BrowserRouter>
  );
}
export default Router;