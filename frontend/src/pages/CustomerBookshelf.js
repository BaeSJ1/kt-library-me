import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import BookCard from '../components/BookCard';
import { platformAPI } from '../services/api';
import styles from '../styles/CustomerBooks.module.css'; // CustomerBooks 스타일 재사용!

const CustomerBookshelf = () => {
  const navigate = useNavigate();
  const [myBooks, setMyBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [user, setUser] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState('전체');

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const parsedUser = JSON.parse(userData);
      setUser(parsedUser);
      
      if (parsedUser.role === 'customer' && parsedUser.id) {
        fetchMyBooks(parsedUser.id);
      } else {
        setError('고객 계정으로 로그인해주세요.');
        setLoading(false);
      }
    } else {
      navigate('/login');
    }
  }, [navigate]);

  const fetchMyBooks = async (customerId) => {
    try {
        setLoading(true);
        setError(null);

        // 1) 읽은 기록만 가져오고
        const records  = await platformAPI.getCustomerBooks(customerId);
        // 2) 전체 도서 목록도 가져와서
        const allBooks = await platformAPI.getAllBooks();

        // 3) 기록에 해당하는 책들만 필터링
        const detailed = allBooks.filter(b =>
        records.some(r => r.bookId === b.id)
        );

        setMyBooks(detailed);
        } catch (err) {
            console.error(err);
            setError('내 서재 도서를 불러오는데 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

  const handleBookClick = async (book) => {
    try {
      const bookIdToUse = book.id || book.bookId;
      await platformAPI.readBook(bookIdToUse);
      
      if (user && user.id) {
        await fetchMyBooks(user.id);
      }
      
      
      navigate(`/customer/books/${book.id}`);
    } catch (err) {
      console.error('도서 열람 실패:', err);
      alert(err.message || '도서 열람에 실패했습니다.');
    }
  };

  const filteredBooks = selectedCategory === '전체' 
    ? myBooks 
    : myBooks.filter(book => book.category === selectedCategory);

  const categories = ['전체', ...new Set(myBooks.map(book => book.category).filter(Boolean))];

  if (loading) {
    return (
      <div className={styles.container}>
        <div className={styles.loading}>
          <span className="material-icons">hourglass_empty</span>
          <p>내 서재를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.container}>
        <div className={styles.error}>
          <span className="material-icons">error_outline</span>
          <p>{error}</p>
          <button onClick={() => navigate('/customer/books')} className={styles.retryButton}>
            도서 둘러보기
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>내 서재</h1>
        <p>{user?.name}님이 열람한 도서 목록입니다</p>
      </div>

      {/* 카테고리 필터 */}
      {myBooks.length > 0 && (
        <div className={styles.categoryFilter}>
          {categories.map(category => (
            <button
              key={category}
              className={`${styles.categoryBtn} ${selectedCategory === category ? styles.active : ''}`}
              onClick={() => setSelectedCategory(category)}
            >
              {category}
            </button>
          ))}
        </div>
      )}

      {/* 도서 개수 표시 */}
      <div className={styles.bookCount}>
        <span>내 서재 {filteredBooks.length}권</span>
      </div>

      {/* 도서 목록 */}
      {myBooks.length === 0 ? (
        <div className={styles.noBooks}>
          <span className="material-icons">menu_book</span>
          <p>아직 열람한 도서가 없습니다</p>
          <p style={{ fontSize: '0.875rem', marginTop: '0.5rem', color: '#9ca3af' }}>
            도서를 둘러보고 마음에 드는 책을 읽어보세요!
          </p>
          <button 
            onClick={() => navigate('/customer/books')}
            style={{
              marginTop: '1rem',
              padding: '0.5rem 1.5rem',
              backgroundColor: '#5e3dea',
              color: 'white',
              border: 'none',
              borderRadius: '0.375rem',
              cursor: 'pointer',
              fontSize: '0.875rem'
            }}
          >
            도서 둘러보러 가기
          </button>
        </div>
      ) : (
        <div className={styles.booksGrid}>
          {filteredBooks.map((book, index) => (
            <BookCard 
              key={`${book.bookId}-${index}`}
              book={{
                ...book,
                id: book.bookId,
              }} 
              onClick={() => handleBookClick(book)}
              showSummary={false}
              showPrice={false}
              showViewCount={false}
              showBestsellerBadge={true}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default CustomerBookshelf;