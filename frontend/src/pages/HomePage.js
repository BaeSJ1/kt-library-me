import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import BookCard from '../components/BookCard';
import { platformAPI } from '../services/api';
import styles from '../styles/CustomerBooks.module.css';


const HomePage = () => {
  const navigate = useNavigate();
  const [bestsellerBooks, setBestsellerBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchBestsellerBooks();
  }, []);

  const fetchBestsellerBooks = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // 모든 도서를 가져온 후 베스트셀러만 필터링
      const allBooks = await platformAPI.getAllBooks();
      const bestsellers = allBooks.filter(book => book.isBestSeller);
      
      setBestsellerBooks(bestsellers);
    } catch (err) {
      console.error('베스트셀러 도서를 불러오는데 실패했습니다:', err);
      setError('베스트셀러 도서를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };
  
  const handleBookClick = async (book) => {
    try {
      // 도서 열람 API 호출
      await platformAPI.readBook(book.id);
      
      // 도서 정보 새로고침
      await fetchBestsellerBooks();
      
      
      
      // 도서 상세 페이지로 이동하려면:
      navigate(`/customer/books/${book.id}`);
    } catch (err) {
      console.error('도서 열람 실패:', err);
      alert('도서 열람에 실패했습니다.');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
        <h1>KT Library 홈페이지</h1>
        <p>도서 출판 및 구독 플랫폼에 오신 것을 환영합니다!</p>
      </div>
      
      {/* 베스트셀러 섹션 */}
      <div style={{ marginTop: '3rem' }}>
        <h2 style={{ textAlign: 'center', marginBottom: '2rem', color: '#1f2937' }}>
          <span className="material-icons" style={{ verticalAlign: 'middle', marginRight: '0.5rem', color: '#fbbf24' }}>
            star
          </span>
          베스트셀러
        </h2>
        
        {loading ? (
          <div style={{ textAlign: 'center', padding: '3rem' }}>
            <span className="material-icons" style={{ fontSize: '3rem', color: '#9ca3af' }}>
              hourglass_empty
            </span>
            <p style={{ marginTop: '1rem', color: '#6b7280' }}>베스트셀러를 불러오는 중...</p>
          </div>
        ) : error ? (
          <div style={{ textAlign: 'center', padding: '3rem' }}>
            <span className="material-icons" style={{ fontSize: '3rem', color: '#ef4444' }}>
              error_outline
            </span>
            <p style={{ marginTop: '1rem', color: '#ef4444' }}>{error}</p>
            <button 
              onClick={fetchBestsellerBooks}
              style={{
                marginTop: '1rem',
                padding: '0.5rem 1rem',
                backgroundColor: '#ef4444',
                color: 'white',
                border: 'none',
                borderRadius: '0.25rem',
                cursor: 'pointer'
              }}
            >
              다시 시도
            </button>
          </div>
        ) : bestsellerBooks.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '3rem', color: '#6b7280' }}>
            <span className="material-icons" style={{ fontSize: '3rem' }}>
              library_books
            </span>
            <p style={{ marginTop: '1rem' }}>아직 베스트셀러 도서가 없습니다.</p>
            <p style={{ fontSize: '0.875rem', marginTop: '0.5rem' }}>
              도서가 5회 이상 열람되면 베스트셀러가 됩니다!
            </p>
          </div>
        ) : (
          <div className={styles.booksGrid}>
            {bestsellerBooks.map(book => (
              <BookCard 
                key={book.id} 
                book={book} 
                onClick={handleBookClick}
              />
            ))}
          </div>
        )}
      </div>

      {/* 전체 도서 보기 버튼 */}
      <div style={{ textAlign: 'center', marginTop: '3rem' }}>
        <button
          onClick={() => navigate('/customer/books')}
          style={{
            padding: '0.75rem 2rem',
            backgroundColor: '#5e3dea',
            color: 'white',
            border: 'none',
            borderRadius: '0.5rem',
            fontSize: '1rem',
            fontWeight: '500',
            cursor: 'pointer',
            transition: 'all 0.2s',
            display: 'inline-flex',
            alignItems: 'center',
            gap: '0.5rem'
          }}
          onMouseOver={(e) => e.target.style.backgroundColor = '#4a2fb8'}
          onMouseOut={(e) => e.target.style.backgroundColor = '#5e3dea'}
        >
          <span className="material-icons">library_books</span>
          전체 도서 둘러보기
        </button>
      </div>
      
      {/* 관리자 로그인 링크 */}
      <div style={{ 
        marginTop: '4rem', 
        textAlign: 'center', 
        paddingTop: '2rem', 
        borderTop: '1px solid #e5e7eb'
      }}>
        <p style={{ color: '#6b7280', fontSize: '0.875rem', marginBottom: '0.5rem' }}>
          시스템 관리자이신가요?
        </p>
        <button 
          onClick={() => navigate('/admin')}
          style={{ 
            padding: '0.5rem 1rem', 
            backgroundColor: '#dc2626', 
            color: 'white', 
            border: 'none', 
            borderRadius: '0.25rem',
            fontSize: '0.875rem',
            cursor: 'pointer',
            transition: 'background-color 0.2s'
          }}
          onMouseOver={(e) => e.target.style.backgroundColor = '#991b1b'}
          onMouseOut={(e) => e.target.style.backgroundColor = '#dc2626'}
        >
          🔐 관리자 로그인
        </button>
      </div>
    </div>
  );
};

export default HomePage;