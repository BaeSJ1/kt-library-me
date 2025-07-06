// src/pages/BookDetailed.jsx
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { platformAPI } from '../services/api';
import styles from '../styles/BookDetailed.module.css';

function BookDetailed() {
  const { bookId } = useParams();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const data = await platformAPI.getBookById(bookId);
        setBook(data);
        console.log('book 데이터', data); 
      } catch (err) {
        setError(err.message || '도서 정보를 가져오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchBook();
  }, [bookId]);

  if (error) {
    return <div style={{ padding: '2rem', color: 'red' }}>❌ {error}</div>;
  }

  if (!book) {
    return <div style={{ padding: '2rem' }}>도서 정보가 존재하지 않습니다.</div>;
  }

  return (
    <div className={styles.card}>
      <div className={styles.content}>
        {/* 좌측: 이미지 */}
        <div className={styles.imageBox}>
          <img src={book.coverUrl} alt="책 표지" className={styles.coverImage} />
        </div>

        {/* 우측: 설명 */}
        <div className={styles.details}>
          <h2>{book.title}</h2>
          <p><strong>저자:</strong> {book.authorName}</p>
          <p><strong>저자 소개:</strong> {book.introduction}</p>
          <p><strong>카테고리:</strong> {book.category}</p>
          <p><strong>요약:</strong> {book.summary}</p>
          <p><strong>가격:</strong> {book.price}</p>
          <p><strong>조회수:</strong> {book.viewCount}</p>
          <p><strong>베스트셀러:</strong> {book.isBestSeller ? '✅' : '❌'}</p>
        </div>
      </div>

      {/* 하단: PDF 링크 */}
      <div className={styles.downloadBox}>
        <a href={book.fileUrl} target="_blank" rel="noopener noreferrer" download>
          📥 PDF 파일 다운로드
        </a>
      </div>
    </div>
  );
}

export default BookDetailed;
