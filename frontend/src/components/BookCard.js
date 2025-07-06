import React from 'react';
import styles from '../styles/BookCard.module.css';

const BookCard = ({ 
  book, 
  onClick, 
  showAuthor = true, 
  showSummary = true, 
  showPrice = true, 
  showViewCount = true, 
  showBestsellerBadge = true 
}) => {
  const {
    id,
    title,
    authorName,
    category,
    summary,
    coverUrl,
    price,
    viewCount,
    isBestSeller,
    fileUrl
  } = book;

  const formatPrice = (price) => {
    if (typeof price !== 'number') return '가격 정보 없음';
    if (price === 0) return '무료';
    return `${price.toLocaleString()}원`;
  };

  const formatViewCount = (count) => {
    if (count >= 1000) {
      return `${(count / 1000).toFixed(1)}k`;
    }
    return count.toString();
  };

  return (
    <div className={styles.bookCard} onClick={() => onClick && onClick(book)}>
      {/* 표지 이미지 */}
      <div className={styles.bookCover}>
        {coverUrl ? (
          <img src={coverUrl} alt={title} className={styles.coverImage} />
        ) : (
          <div className={styles.coverPlaceholder}>
            <span className="material-icons">menu_book</span>
          </div>
        )}
        
        {/* 베스트셀러 뱃지 */}
        {showBestsellerBadge && isBestSeller && (
          <div className={styles.bestsellerBadge}>
            <span className="material-icons">star</span>
            <span>베스트</span>
          </div>
        )}
        
        {/* 카테고리 태그 */}
        {category && (
          <div className={styles.categoryTag}>
            {category}
          </div>
        )}
      </div>

      {/* 도서 정보 */}
      <div className={styles.bookInfo}>
        <h3 className={styles.bookTitle}>{title}</h3>
        {showAuthor && (
          <p className={styles.bookAuthor}>
            <span className="material-icons">person</span>
            {authorName}
          </p>
        )}
        
        {showSummary && summary && (
          <p className={styles.bookSummary}>
            {summary.length > 80 ? `${summary.substring(0, 80)}...` : summary}
          </p>
        )}
        
        <div className={styles.bookMeta}>
          <div className={styles.bookStats}>
            {showViewCount && (
              <span className={styles.viewCount}>
                <span className="material-icons">visibility</span>
                {formatViewCount(viewCount)}
              </span>
            )}
          </div>
          
          {showPrice && (
            <div className={styles.bookPrice}>
              {formatPrice(price)}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default BookCard;