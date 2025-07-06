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

// // 테스트용 도서 데이터
// const sampleBooks = [
//   {
//     id: 1,
//     title: "클린 코드: 애자일 소프트웨어 장인 정신",
//     authorName: "로버트 C. 마틴",
//     category: "프로그래밍",
//     summary: "더 나은 코드를 작성하는 방법에 대한 실용적인 조언들을 담은 개발자 필독서입니다. 좋은 코드와 나쁜 코드를 구분하는 방법을 배우고, 좋은 코드를 작성하는 실무 기법을 익힐 수 있습니다.",
//     coverUrl: null,
//     price: 1500,
//     viewCount: 6,
//     isBestSeller: true,
//     fileUrl: "/books/clean-code.pdf"
//   },
//   {
//     id: 2,
//     title: "자바스크립트 완벽 가이드",
//     authorName: "데이비드 플래나간",
//     category: "웹개발",
//     summary: "자바스크립트의 모든 것을 다룬 완벽한 레퍼런스 가이드입니다. 기초부터 고급 주제까지 체계적으로 학습할 수 있습니다.",
//     coverUrl: null,
//     price: 1000,
//     viewCount: 3,
//     isBestSeller: false,
//     fileUrl: "/books/javascript-guide.pdf"
//   }
// ];

// 임시 페이지 컴포넌트들
// const HomePage = () => {
//   const navigate = useNavigate();
  
//   const handleBookClick = (book) => {
//     console.log('책 클릭:', book);
//     alert(`"${book.title}" 책을 선택했습니다!`);
//   };

//   return (
//     <div style={{ padding: '2rem' }}>
//       <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
//         <h1>KT Library 홈페이지</h1>
//         <p>도서 출판 및 구독 플랫폼에 오신 것을 환영합니다!</p>
//       </div>
      
//       {/* 도서 카드 테스트 섹션 */}
//       <div style={{ marginTop: '3rem' }}>
//         <h2 style={{ textAlign: 'center', marginBottom: '2rem', color: '#1f2937' }}>인기 도서</h2>
//         <div className="books-grid">
//           {sampleBooks.map(book => (
//             <BookCard 
//               key={book.id} 
//               book={book} 
//               onClick={handleBookClick}
//             />
//           ))}
//         </div>
//       </div>
      
//       {/* 관리자 로그인 링크 */}
//       <div style={{ 
//         marginTop: '4rem', 
//         textAlign: 'center', 
//         paddingTop: '2rem', 
//         borderTop: '1px solid #e5e7eb'
//       }}>
//         <p style={{ color: '#6b7280', fontSize: '0.875rem', marginBottom: '0.5rem' }}>
//           시스템 관리자이신가요?
//         </p>
//         <button 
//           onClick={() => navigate('/admin')}
//           style={{ 
//             padding: '0.5rem 1rem', 
//             backgroundColor: '#dc2626', 
//             color: 'white', 
//             border: 'none', 
//             borderRadius: '0.25rem',
//             fontSize: '0.875rem',
//             cursor: 'pointer',
//             transition: 'background-color 0.2s'
//           }}
//           onMouseOver={(e) => e.target.style.backgroundColor = '#991b1b'}
//           onMouseOut={(e) => e.target.style.backgroundColor = '#dc2626'}
//         >
//           🔐 관리자 로그인
//         </button>
//       </div>
//     </div>
//   );
// };



// 작가 페이지들
// const AuthorManuscripts = () => (
//   <div style={{ padding: '2rem' }}>
//     <h1>내 원고</h1>
//     <p>작가의 원고 목록입니다.</p>
//   </div>
// );

// 관리자 페이지들 - AuthorApproval 컴포넌트를 사용

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

// const AuthorPublishing = () => (
//   <div style={{ padding: '2rem' }}>
//     <h1>출판 현황</h1>
//     <p>출판 현황을 확인하는 페이지입니다.</p>
//   </div>
// );



// 고객 페이지들
// const CustomerBooks = () => (
//   <div style={{ padding: '2rem' }}>
//     <h1>도서 둘러보기</h1>
//     <p>도서를 둘러보는 페이지입니다.</p>
//   </div>
// );

// const CustomerBookshelf = () => (
//   <div style={{ padding: '2rem' }}>
//     <h1>내 서재</h1>
//     <p>내 서재 페이지입니다.</p>
//   </div>
// );

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