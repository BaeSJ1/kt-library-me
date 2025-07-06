import React from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import Navigation from './components/Navigation';
import BookCard from './components/BookCard';
import Login from './pages/Login';
import Register from './pages/Register';
import AdminLogin from './pages/AdminLogin';
import AuthorApproval from './pages/AuthorApproval';
import AuthorProfile from './pages/AuthorProfile';
import SubscriptionPage from './pages/SubscriptionPage';
import AuthorManuscripts from './pages/AuthorManuscripts';
import AuthorNewManuscript from './pages/AuthorNewManuscript';
import AuthorPublishing from './pages/AuthorPublishing';
import CustomerPoints from './pages/CustomerPoints';
import CustomerBooks from './pages/CustomerBooks';
import HomePage from './pages/HomePage';
import CustomerBookshelf from './pages/CustomerBookshelf';
import BookDetailed from './pages/BookDetailed'; 

import './App.css';

const AdminBooks = () => (
  <div style={{ padding: '2rem' }}>
    <h1>도서 관리</h1>
    <p>도서 관리 페이지입니다.</p>
  </div>
);

const AdminCustomers = () => (
  <div style={{ padding: '2rem' }}>
    <h1>고객 관리</h1>
    <p>고객 관리 페이지입니다.</p>
  </div>
);


const CustomerSubscription = () => (
  <div style={{ padding: '2rem' }}>
    <h1>구독 관리</h1>
    <p>구독 관리 페이지입니다.</p>
  </div>
);



function App() {
  return (
    <Router>
      <div className="App">
        <Navigation />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<HomePage />} />
          
          {/* 작가 라우트 */}
          <Route path="/author/manuscripts" element={<AuthorManuscripts />} />
          <Route path="/author/new-manuscript" element={<AuthorNewManuscript />} />
          <Route path="/author/publishing" element={<AuthorPublishing />} />
          <Route path="/author/profile" element={<AuthorProfile />} />
          
          {/* 고객 라우트 */}
          <Route path="/customer/books" element={<CustomerBooks />} />
          <Route path="/customer/bookshelf" element={<CustomerBookshelf />} />
          <Route path="/customer/subscription" element={<SubscriptionPage />} />
          <Route path="/customer/points" element={<CustomerPoints />} />
          <Route path="/customer/bookshelf" element={<CustomerBookshelf />} />
          <Route path="/customer/books/:bookId" element={<BookDetailed />} />

          {/* 관리자 라우트 */}
          <Route path="/admin" element={<AdminLogin />} />
          <Route path="/admin/authors" element={<AuthorApproval />} />
          <Route path="/admin/books" element={<AdminBooks />} />
          <Route path="/admin/customers" element={<AdminCustomers />} />

        </Routes>
      </div>
    </Router>
  );
}

export default App;