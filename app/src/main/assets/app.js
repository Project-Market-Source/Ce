// Cinemana Simulated ISP & Network Database
const MOCK_STREAMS = {
    "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
    "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
};

const MOCK_CATEGORIES = [
    { id: "1", name: "Action Thrillers", ic: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="category-icon"><path d="M8.5 14.5A2.5 2.5 0 0 0 11 12c0-1.38-.5-2-1-3-1.072-2.143-.224-4.054 2-6 .5 2.5 2 4.9 4 6.5 2 1.6 3 3.5 3 5.5a7 7 0 1 1-14 0c0-1.153.433-2.294 1-3a2.5 2.5 0 0 0 2.5 2.5z"/></svg>' },
    { id: "2", name: "Sci-Fi Space", ic: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="category-icon"><path d="M4.5 16.5c-1.5 1.26-2.5 3.19-2.5 5.5h20c0-2.31-1-4.24-2.5-5.5M12 2L9 9h6L12 2z"/><path d="M12 16.5a4.5 4.5 0 1 0 0-9 4.5 4.5 0 0 0 0 9z"/></svg>' },
    { id: "3", name: "Crime & Drama", ic: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="category-icon"><path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z"/><path d="M8 14s1.5 2 4 2 4-2 4-2M9 9h.01M15 9h.01"/></svg>' },
    { id: "4", name: "Trending Shows", ic: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="category-icon"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"></polyline><polyline points="17 6 23 6 23 12"></polyline></svg>' },
    { id: "5", name: "Local ISP Premium", ic: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="category-icon"><rect x="2" y="2" width="20" height="8" rx="2" ry="2"></rect><rect x="2" y="14" width="20" height="8" rx="2" ry="2"></rect><line x1="6" y1="6" x2="6.01" y2="6"></line><line x1="6" y1="18" x2="6.01" y2="18"></line></svg>' },
    { id: "6", name: "Recently Added", ic: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="category-icon"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>' }
];

const MOCK_MOVIES = [
    {
        id: "m1",
        title: "Tears of Steel",
        type: "movie",
        year: "2024",
        rating: "8.4",
        genres: ["Sci-Fi", "Action", "Cyberpunk"],
        synopsis: "Set in a dystopian future where robots have seized control, a rogue band of software engineers and warriors try to salvage humanity's fate using forgotten neural arrays in the heart of Amsterdam.",
        posterUrl: "https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?auto=format&fit=crop&q=80&w=600",
        duration: "1h 52m",
        streamUrls: MOCK_STREAMS
    },
    {
        id: "m2",
        title: "The Cosmos Within",
        type: "movie",
        year: "2025",
        rating: "9.1",
        genres: ["Space", "Sci-Fi", "Documentary"],
        synopsis: "A cinematic journey exploring the boundless expanses of our outer cosmos, contrasted against the deep internal neural pathways of human synapses.",
        posterUrl: "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&q=80&w=600",
        duration: "2h 10m",
        streamUrls: {
            "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
        }
    },
    {
        id: "m3",
        title: "Tokyo Driftway",
        type: "movie",
        year: "2023",
        rating: "7.8",
        genres: ["Action", "Racing", "Suspense"],
        synopsis: "Underground racing teams navigate neon-lit alleys of neo-Tokyo using prototype vehicles built on local network optical nodes, bypassing city surveillance networks.",
        posterUrl: "https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&q=80&w=600",
        duration: "1h 45m",
        streamUrls: MOCK_STREAMS
    },
    {
        id: "m4",
        title: "Neon Sins",
        type: "series",
        year: "2024",
        rating: "8.9",
        genres: ["Crime", "Mystery", "Noir"],
        synopsis: "A private investigator on a cybernetic city block is hired to track down missing databases holding the key to the local server grids supplying offline video libraries.",
        posterUrl: "https://images.unsplash.com/photo-1514306191717-452ec28c7814?auto=format&fit=crop&q=80&w=600",
        duration: "1 Season",
        streamUrls: MOCK_STREAMS,
        episodes: [
            {
                id: "e1_1",
                title: "The Grid Unlocked",
                episodeNumber: 1,
                seasonNumber: 1,
                streamUrls: {
                    "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                }
            },
            {
                id: "e1_2",
                title: "Dark Fiber",
                episodeNumber: 2,
                seasonNumber: 1,
                streamUrls: {
                    "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
                }
            },
            {
                id: "e1_3",
                title: "The CDN Heist",
                episodeNumber: 3,
                seasonNumber: 1,
                streamUrls: MOCK_STREAMS
            }
        ]
    },
    {
        id: "m5",
        title: "Whispers in the Dust",
        type: "movie",
        year: "2024",
        rating: "7.9",
        genres: ["Thriller", "Drama"],
        synopsis: "In an abandoned wind-farm in central Arizona, researchers discover encrypted low-frequency audio tracks that tell a story of an impending cosmic rupture.",
        posterUrl: "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&q=80&w=600",
        duration: "1h 51m",
        streamUrls: MOCK_STREAMS
    },
    {
        id: "m6",
        title: "Deep Space Heist",
        type: "series",
        year: "2025",
        rating: "8.6",
        genres: ["Sci-Fi", "Comedy"],
        synopsis: "A band of cosmic mercenaries attempts to steal server archives holding ancient classical terrestrial video files. A humorous, action-packed high-speed space chase.",
        posterUrl: "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&q=80&w=600",
        duration: "2 Seasons",
        streamUrls: MOCK_STREAMS,
        episodes: [
            {
                id: "e2_1",
                title: "Pilot: Storage Full",
                episodeNumber: 1,
                seasonNumber: 1,
                streamUrls: MOCK_STREAMS
            },
            {
                id: "e2_2",
                title: "The Helium Vault",
                episodeNumber: 2,
                seasonNumber: 1,
                streamUrls: MOCK_STREAMS
            }
        ]
    },
    {
        id: "m7",
        title: "Retro Beats",
        type: "movie",
        year: "2024",
        rating: "8.2",
        genres: ["Music", "Documentary"],
        synopsis: "Chronicles the evolution of early electronic modular music and its modern-day surge on indie internet broadcasts across isolated community relays.",
        posterUrl: "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&q=80&w=600",
        duration: "1h 35m",
        streamUrls: MOCK_STREAMS
    }
];

// App Variables & Global State
let cdnUrl = localStorage.getItem("cinemana_cdn_url") || "https://mock.cinemana.local/";
let watchlist = JSON.parse(localStorage.getItem("cinemana_watchlist")) || [];
let currentMovies = [...MOCK_MOVIES];
let currentCategories = [...MOCK_CATEGORIES];
let activeCarouselIndex = 0;
let carouselInterval = null;
let currentMovieDetail = null;
let activePlayingMovie = null;
let activePlayingEpisode = null;
let activePlayingQuality = "1080p";
let isDraggingTimeline = false;

// DOM Cache elements
const domElements = {
    netDot: document.getElementById("net-dot"),
    netText: document.getElementById("net-text"),
    carouselTrack: document.getElementById("carousel-track"),
    rowTrending: document.getElementById("row-trending"),
    rowPremium: document.getElementById("row-premium"),
    rowShows: document.getElementById("row-shows"),
    searchBox: document.getElementById("search-box"),
    searchGrid: document.getElementById("search-results-grid"),
    searchEmpty: document.getElementById("search-empty-state"),
    searchClear: document.getElementById("search-clear-btn"),
    categoriesGrid: document.getElementById("categories-grid"),
    categoryFilterView: document.getElementById("category-filter-view"),
    categoryMoviesGrid: document.getElementById("category-movies-grid"),
    categoryBackBtn: document.getElementById("cat-back-btn"),
    categoryTitle: document.getElementById("cat-filter-title"),
    watchlistGrid: document.getElementById("watchlist-grid"),
    watchlistEmpty: document.getElementById("watchlist-empty-state"),
    cdnInput: document.getElementById("cdn-url-input"),
    diagStatus: document.getElementById("diag-status"),
    diagUrl: document.getElementById("diag-url"),
    diagWatchlist: document.getElementById("diag-watchlist"),
    saveSettingsBtn: document.getElementById("settings-save-btn"),
    resetSettingsBtn: document.getElementById("settings-reset-btn"),
    movieDetailPane: document.getElementById("movie-detail-pane"),
    detailBannerImg: document.getElementById("detail-banner-img"),
    detailBackBtn: document.getElementById("detail-back-btn"),
    detailYear: document.getElementById("detail-year"),
    detailDuration: document.getElementById("detail-duration"),
    detailScore: document.getElementById("detail-score"),
    detailTitle: document.getElementById("detail-title"),
    detailGenres: document.getElementById("detail-genres"),
    detailSynopsis: document.getElementById("detail-synopsis"),
    detailPlayBtn: document.getElementById("detail-play-btn"),
    detailWlBtn: document.getElementById("detail-wl-dtl-btn"),
    wlBtnLabel: document.getElementById("wl-btn-label"),
    wlIconSvg: document.getElementById("wl-icon-svg"),
    detailEpisodesSection: document.getElementById("detail-episodes-section"),
    episodeList: document.getElementById("episode-list"),
    customPlayer: document.getElementById("custom-player"),
    videoElement: document.getElementById("html5-video"),
    playerLoader: document.getElementById("player-loader"),
    playerCloseBtn: document.getElementById("player-close-btn"),
    playerMovieTitle: document.getElementById("player-movie-title"),
    playerEpisodeTitle: document.getElementById("player-episode-title"),
    timelineTrack: document.getElementById("timeline-track"),
    timelineProgress: document.getElementById("timeline-progress"),
    timelineHandle: document.getElementById("timeline-handle"),
    timeCurrent: document.getElementById("player-time-current"),
    timeDuration: document.getElementById("player-time-duration"),
    playPauseBtn: document.getElementById("player-play-pause-btn"),
    playSvg: document.getElementById("play-svg"),
    pauseSvg: document.getElementById("pause-svg"),
    rewindBtn: document.getElementById("player-rewind-btn"),
    forwardBtn: document.getElementById("player-forward-btn"),
    qualityBtn: document.getElementById("player-quality-btn"),
    qualityDropdown: document.getElementById("quality-dropdown"),
    fullscreenBtn: document.getElementById("player-fullscreen-btn"),
};

// Initial setup
document.addEventListener("DOMContentLoaded", () => {
    initApp();
});

function initApp() {
    setupTabNavigation();
    setupNetworkStatus();
    loadCdnSettings();
    updateAppDatabase();
    setupMovieInteractions();
    setupImmersivePlayer();
    setupHistoryState();
    startCarouselTimer();
}

// History push state support to handle backward navigation perfectly inside android webview!
function setupHistoryState() {
    // Standard initial route state
    window.history.replaceState({ screen: 'dashboard' }, '');

    window.onpopstate = (event) => {
        if (!event.state) return;
        
        // Handle closing screens or drawers depending on popstate
        if (event.state.screen === 'dashboard') {
            closeMovieDetail(false);
            closeVideoPlayer(false);
        } else if (event.state.screen === 'detail') {
            closeVideoPlayer(false);
            if (currentMovieDetail) {
                openMovieDetail(currentMovieDetail.id, false);
            }
        }
    };
}

// Universal tab switcher
function setupTabNavigation() {
    const tabs = document.querySelectorAll(".nav-item");
    const screens = document.querySelectorAll(".app-screen");

    tabs.forEach(tab => {
        tab.addEventListener("click", () => {
            const tabId = tab.getAttribute("data-tab");
            
            // Toggle active tabs
            tabs.forEach(t => t.classList.remove("active"));
            tab.classList.add("active");

            // Toggle active screens
            screens.forEach(s => s.classList.remove("active"));
            const targetScreen = document.getElementById(`screen-${tabId}`);
            if (targetScreen) {
                targetScreen.classList.add("active");
                
                // Extra trigger actions per panel
                if (tabId === "watchlist") {
                     renderWatchlist();
                } else if (tabId === "settings") {
                     updateDiagnostics();
                } else if (tabId === "categories") {
                    // Reset filter view if user taps core Categories navigation tab
                    domElements.categoryFilterView.classList.add("hidden");
                    domElements.categoriesGrid.classList.remove("hidden");
                }
            }
            
            // Clean up details and player if user navigated away
            closeMovieDetail(false);
        });
    });
}

// Handles local CDN settings update
function loadCdnSettings() {
    domElements.cdnInput.value = cdnUrl === "https://mock.cinemana.local/" ? "" : cdnUrl;
    
    domElements.saveSettingsBtn.addEventListener("click", () => {
        let inputVal = domElements.cdnInput.value.trim();
        if (!inputVal) {
            cdnUrl = "https://mock.cinemana.local/";
        } else {
            if (!inputVal.endsWith("/")) {
                inputVal += "/";
            }
            if (!inputVal.startsWith("http://") && !inputVal.startsWith("https://")) {
                inputVal = "http://" + inputVal;
            }
            cdnUrl = inputVal;
        }
        localStorage.setItem("cinemana_cdn_url", cdnUrl);
        updateAppDatabase();
        showToast("CDN URL updated successfully!");
    });

    domElements.resetSettingsBtn.addEventListener("click", () => {
        cdnUrl = "https://mock.cinemana.local/";
        domElements.cdnInput.value = "";
        localStorage.setItem("cinemana_cdn_url", cdnUrl);
        updateAppDatabase();
        showToast("Switched to offline Safe Simulator!");
    });
}

// ISP CDN Fetcher System with protected Fail-proof fallback
async function updateAppDatabase() {
    updateDiagnostics();

    if (cdnUrl === "https://mock.cinemana.local/") {
        setSimulatedState(true);
        currentMovies = [...MOCK_MOVIES];
        currentCategories = [...MOCK_CATEGORIES];
        renderDashboard();
        return;
    }

    setSimulatedState(false, "Connecting CDN...");

    try {
        // Build query promises to fetch files in parallel
        const moviesPromise = fetch(cdnUrl + "movies").then(res => res.json());
        const categoriesPromise = fetch(cdnUrl + "categories").then(res => res.json());

        // Wait with a secure timeline timeout
        const data = await Promise.all([
            promiseTimeout(3500, moviesPromise),
            promiseTimeout(3500, categoriesPromise)
        ]);

        currentMovies = data[0];
        currentCategories = data[1].map(cat => ({
            id: cat.id,
            name: cat.name,
            ic: getCategoryIconSvg(cat.iconName)
        }));

        setSimulatedState(false, "Live Earthlink Connect");
        showToast("Connected to live ISP CDN!");
    } catch (err) {
        console.error("Local network CDN connection failed, loading offline fallback models.", err);
        setSimulatedState(true, "Offline Fallback Live");
        currentMovies = [...MOCK_MOVIES];
        currentCategories = [...MOCK_CATEGORIES];
        showToast("CDN unreachable. Swapped to ISP simulation!");
    }

    renderDashboard();
}

function promiseTimeout(ms, promise) {
    return new Promise((resolve, reject) => {
        const timer = setTimeout(() => reject(new Error('Network CDN connection timeout')), ms);
        promise.then(
            (res) => { clearTimeout(timer); resolve(res); },
            (err) => { clearTimeout(timer); reject(err); }
        );
    });
}

function setSimulatedState(isSim, txt = "") {
    if (isSim) {
        domElements.netDot.style.backgroundColor = "var(--primary)";
        domElements.netDot.style.boxShadow = "0 0 8px var(--primary)";
        domElements.netText.textContent = txt || "Safe Simulator";
        domElements.diagStatus.textContent = "Safe Simulator Mode";
        domElements.diagStatus.className = "val text-success";
    } else {
        domElements.netDot.style.backgroundColor = "var(--secondary)";
        domElements.netDot.style.boxShadow = "0 0 8px var(--secondary)";
        domElements.netText.textContent = txt || "Live ISP Connected";
        domElements.diagStatus.textContent = "Live CDN Server Mode";
        domElements.diagStatus.className = "val";
    }
}

function updateDiagnostics() {
    domElements.diagUrl.textContent = cdnUrl;
    domElements.diagWatchlist.textContent = watchlist.length;
}

// Return dynamic category icons SVG
function getCategoryIconSvg(name) {
    const matched = MOCK_CATEGORIES.find(c => c.name.toLowerCase().includes(name.toLowerCase()) || name.toLowerCase().includes(c.name.toLowerCase()));
    return matched ? matched.ic : '<svg viewBox="0 0 24 24" fill="none" class="category-icon" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"></rect><rect x="14" y="3" width="7" height="7"></rect><rect x="14" y="14" width="7" height="7"></rect><rect x="3" y="14" width="7" height="7"></rect></svg>';
}

// Rendering components inside View Feed
function renderDashboard() {
    renderCarousel();
    renderScrollingRows();
    renderCategoriesGrid();
}

// Carousel Carousel slider
function renderCarousel() {
    const featured = currentMovies.slice(0, 3);
    domElements.carouselTrack.innerHTML = "";
    
    featured.forEach((m, idx) => {
        const slide = document.createElement("div");
        slide.className = `hero-slide ${idx === 0 ? 'active' : ''}`;
        slide.style.backgroundImage = `url('${m.posterUrl}')`;
        
        slide.innerHTML = `
            <div class="hero-slide-overlay">
                <div class="hero-badge-row">
                    <span class="h-badge">TOP RATED</span>
                    <span class="h-badge h-badge-outline">&#9733; ${m.rating}</span>
                </div>
                <h1>${m.title}</h1>
                <p>${m.synopsis}</p>
                <button class="btn btn-primary hero-p-btn" style="max-width: 140px; font-size: 11.5px; padding: 8px 10px;" onclick="openMovieDetail('${m.id}')">
                    <svg viewBox="0 0 24 24" fill="currentColor" width="14" height="14"><polygon points="5 3 19 12 5 21 5 3"></polygon></svg> Detail Pane
                </button>
            </div>
        `;
        domElements.carouselTrack.appendChild(slide);
    });

    renderCarouselDots();
}

function renderCarouselDots() {
    const dotsContainer = document.querySelector(".carousel-nav");
    dotsContainer.innerHTML = "";
    const featured = currentMovies.slice(0, 3);
    featured.forEach((_, idx) => {
        const dot = document.createElement("span");
        dot.className = `carousel-dot ${idx === 0 ? 'active' : ''}`;
        dot.addEventListener("click", () => showCarouselImage(idx));
        dotsContainer.appendChild(dot);
    });
}

function startCarouselTimer() {
    if (carouselInterval) clearInterval(carouselInterval);
    carouselInterval = setInterval(() => {
        let nextIndex = (activeCarouselIndex + 1) % 3;
        showCarouselImage(nextIndex);
    }, 5000);
}

function showCarouselImage(idx) {
    activeCarouselIndex = idx;
    const slides = document.querySelectorAll(".hero-slide");
    const dots = document.querySelectorAll(".carousel-dot");
    
    if (slides.length === 0) return;

    slides.forEach(s => s.classList.remove("active"));
    dots.forEach(d => d.classList.remove("active"));

    if (slides[idx]) slides[idx].classList.add("active");
    if (dots[idx]) dots[idx].classList.add("active");
}

// Horizontal scrolling widgets render
function renderScrollingRows() {
    // Row 1: Trending
    const trending = currentMovies.filter(m => parseFloat(m.rating) >= 8.2);
    fillRow(domElements.rowTrending, trending);

    // Row 2: Premium movies
    const premium = currentMovies.filter(m => m.type === "movie");
    fillRow(domElements.rowPremium, premium);

    // Row 3: Shows
    const shows = currentMovies.filter(m => m.type === "series");
    fillRow(domElements.rowShows, shows);
}

function fillRow(container, movies) {
    container.innerHTML = "";
    if (movies.length === 0) {
        container.innerHTML = `<span style="padding:10px; color:var(--text-muted); font-size:12px">No titles available</span>`;
        return;
    }
    
    movies.forEach(m => {
        const card = createMovieCardMarkup(m);
        container.appendChild(card);
    });
}

function createMovieCardMarkup(m) {
    const card = document.createElement("div");
    card.className = "movie-card";
    card.setAttribute("data-id", m.id);
    card.addEventListener("click", () => openMovieDetail(m.id));

    card.innerHTML = `
        <div class="card-poster" style="background-image: url('${m.posterUrl}')">
            <div class="card-rating-badge">&#9733; ${m.rating}</div>
        </div>
        <div class="card-info">
            <h4 class="card-title">${m.title}</h4>
            <span class="card-sub">${m.year} &bull; ${m.type === 'movie' ? 'Movie' : 'Series'}</span>
        </div>
    `;
    return card;
}

// Categories panels display
function renderCategoriesGrid() {
    domElements.categoriesGrid.innerHTML = "";
    currentCategories.forEach(cat => {
        const block = document.createElement("div");
        block.className = "category-block";
        block.innerHTML = `
            ${cat.ic}
            <span>${cat.name}</span>
        `;
        block.addEventListener("click", () => {
            openCategoryFilter(cat);
        });
        domElements.categoriesGrid.appendChild(block);
    });
}

function openCategoryFilter(category) {
    domElements.categoriesGrid.classList.add("hidden");
    domElements.categoryFilterView.classList.remove("hidden");
    domElements.categoryTitle.textContent = category.name;

    // Filter list
    const filtered = currentMovies.filter(m => 
        m.genres.some(g => category.name.toLowerCase().includes(g.toLowerCase()) || g.toLowerCase().includes(category.name.toLowerCase())) || 
        (category.name.toLowerCase().includes("shows") && m.type === "series") ||
        (category.name.toLowerCase().includes("premium") && m.type === "movie") ||
        (category.name.toLowerCase().includes("recently") && parseFloat(m.rating) > 8.0)
    );

    domElements.categoryMoviesGrid.innerHTML = "";
    if (filtered.length === 0) {
        domElements.categoryMoviesGrid.innerHTML = `<div class="empty-state show" style="grid-column: 1/-1; display:flex"><h4>Empty Category</h4><p>No titles match this filter group at the moment.</p></div>`;
        return;
    }

    filtered.forEach(m => {
        domElements.categoryMoviesGrid.appendChild(createMovieCardMarkup(m));
    });
}

domElements.categoryBackBtn.addEventListener("click", () => {
    domElements.categoryFilterView.classList.add("hidden");
    domElements.categoriesGrid.classList.remove("hidden");
});

// Watchlist Screen
function renderWatchlist() {
    domElements.watchlistGrid.innerHTML = "";
    if (watchlist.length === 0) {
        domElements.watchlistEmpty.style.display = "flex";
        domElements.watchlistGrid.style.display = "none";
        return;
    }
    
    domElements.watchlistEmpty.style.display = "none";
    domElements.watchlistGrid.style.display = "grid";
    
    watchlist.forEach(m => {
        domElements.watchlistGrid.appendChild(createMovieCardMarkup(m));
    });
}

// Interactive Search Screen setup
function setupMovieInteractions() {
    let searchTimeout = null;

    domElements.searchBox.addEventListener("input", (e) => {
        const val = e.target.value.trim().toLowerCase();
        
        if (val) {
            domElements.searchClear.classList.remove("hidden");
        } else {
            domElements.searchClear.classList.add("hidden");
        }

        if (searchTimeout) clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            performSearch(val);
        }, 150);
    });

    domElements.searchClear.addEventListener("click", () => {
        domElements.searchBox.value = "";
        domElements.searchClear.classList.add("hidden");
        performSearch("");
    });
}

function performSearch(query) {
    if (!query) {
        domElements.searchGrid.innerHTML = "";
        domElements.searchEmpty.style.display = "flex";
        domElements.searchGrid.style.display = "none";
        return;
    }

    const filtered = currentMovies.filter(m => 
        m.title.toLowerCase().includes(query) ||
        m.synopsis.toLowerCase().includes(query) ||
        m.genres.some(g => g.toLowerCase().includes(query))
    );

    domElements.searchGrid.innerHTML = "";
    if (filtered.length === 0) {
        domElements.searchEmpty.style.display = "flex";
        domElements.searchGrid.style.display = "none";
    } else {
        domElements.searchEmpty.style.display = "none";
        domElements.searchGrid.style.display = "grid";
        filtered.forEach(m => {
            domElements.searchGrid.appendChild(createMovieCardMarkup(m));
        });
    }
}

// Movie Detail Drawer display logic
async function openMovieDetail(id, pushState = true) {
    let target = currentMovies.find(m => m.id === id);
    if (!target) return;

    currentMovieDetail = target;

    // Fill fields
    domElements.detailBannerImg.style.backgroundImage = `url('${target.posterUrl}')`;
    domElements.detailYear.textContent = target.year;
    domElements.detailDuration.textContent = target.duration;
    domElements.detailScore.textContent = target.rating;
    domElements.detailTitle.textContent = target.title;
    domElements.detailSynopsis.textContent = target.synopsis;

    // Genres Tags
    domElements.detailGenres.innerHTML = "";
    target.genres.forEach(g => {
        const span = document.createElement("span");
        span.className = "genre-tag";
        span.textContent = g;
        domElements.detailGenres.appendChild(span);
    });

    // Watchlist state button style
    const isWl = watchlist.some(m => m.id === target.id);
    updateWatchlistBtnStyle(isWl);

    // Click handler for watchlist add/remove toggle
    domElements.detailWlBtn.onclick = () => {
        const idx = watchlist.findIndex(m => m.id === target.id);
        if (idx > -1) {
            watchlist.splice(idx, 1);
            localStorage.setItem("cinemana_watchlist", JSON.stringify(watchlist));
            updateWatchlistBtnStyle(false);
            showToast("Removed from Watchlist");
        } else {
            watchlist.push(target);
            localStorage.setItem("cinemana_watchlist", JSON.stringify(watchlist));
            updateWatchlistBtnStyle(true);
            showToast("Added to Watchlist!");
        }
    };

    // Play action
    domElements.detailPlayBtn.onclick = () => {
        if (target.type === "series" && target.episodes && target.episodes.length > 0) {
            playVideo(target, target.episodes[0]);
        } else {
            playVideo(target);
        }
    };

    // Render Tv web episodes if TV Series type
    if (target.type === "series" && target.episodes && target.episodes.length > 0) {
        domElements.detailEpisodesSection.classList.remove("hidden");
        domElements.episodeList.innerHTML = "";
        
        target.episodes.forEach(ep => {
            const tile = document.createElement("div");
            tile.className = "episode-tile";
            tile.innerHTML = `
                <div>
                    <div class="ep-number">Season ${ep.seasonNumber} &bull; Episode ${ep.episodeNumber}</div>
                    <div class="ep-title">${ep.title}</div>
                </div>
                <svg viewBox="0 0 24 24" fill="none" class="category-icon" stroke="currentColor" stroke-width="2.5" style="width:16px; height:16px"><polygon points="5 3 19 12 5 21 5 3" fill="currentColor"></polygon></svg>
            `;
            tile.addEventListener("click", () => {
                playVideo(target, ep);
            });
            domElements.episodeList.appendChild(tile);
        });
    } else {
        domElements.detailEpisodesSection.classList.add("hidden");
    }

    // Modal show trigger
    domElements.movieDetailPane.classList.remove("hidden");
    setTimeout(() => {
        domElements.movieDetailPane.classList.add("active");
    }, 10);

    // Browser back support integration
    if (pushState) {
        window.history.pushState({ screen: 'detail', id }, '');
    }
}

function updateWatchlistBtnStyle(isInWl) {
    if (isInWl) {
        domElements.detailWlBtn.className = "wl-btn added";
        domElements.wlBtnLabel.textContent = "Saved to Watchlist";
        domElements.wlIconSvg.setAttribute("fill", "currentColor");
    } else {
        domElements.detailWlBtn.className = "wl-btn";
        domElements.wlBtnLabel.textContent = "To Watchlist";
        domElements.wlIconSvg.setAttribute("fill", "none");
    }
}

function closeMovieDetail(popHistory = true) {
    domElements.movieDetailPane.classList.remove("active");
    setTimeout(() => {
        domElements.movieDetailPane.classList.add("hidden");
    }, 300);
    
    currentMovieDetail = null;

    if (popHistory && window.history.state && window.history.state.screen === 'detail') {
        window.history.back();
    }
}

domElements.detailBackBtn.addEventListener("click", () => {
    closeMovieDetail();
});


// Custom Video Player controls binding
function setupImmersivePlayer() {
    const video = domElements.videoElement;

    // Auto-update playback progress inside track
    video.addEventListener("timeupdate", () => {
        if (!isDraggingTimeline) {
            updatePlayerTimelineDisplays();
        }
    });

    // Time durations load
    video.addEventListener("durationchange", () => {
        domElements.timeDuration.textContent = formatTime(video.duration);
    });

    // Loading status spinners toggle
    video.addEventListener("waiting", () => {
        domElements.playerLoader.classList.remove("hidden");
    });
    video.addEventListener("playing", () => {
        domElements.playerLoader.classList.add("hidden");
    });
    video.addEventListener("canplay", () => {
        domElements.playerLoader.classList.add("hidden");
    });

    // Play Pause central click action
    domElements.playPauseBtn.addEventListener("click", () => {
        togglePlayPause();
    });

    // Rewind Fast actions
    domElements.rewindBtn.addEventListener("click", () => {
        video.currentTime = Math.max(0, video.currentTime - 10);
        showToast("Rewind 10s");
    });

    domElements.forwardBtn.addEventListener("click", () => {
        video.currentTime = Math.min(video.duration || 0, video.currentTime + 10);
        showToast("Forward 10s");
    });

    // Fullscreen scaling toggles
    domElements.fullscreenBtn.addEventListener("click", () => {
        if (video.requestFullscreen) {
            video.requestFullscreen();
        } else if (video.webkitRequestFullscreen) {
            video.webkitRequestFullscreen();
        } else if (domElements.customPlayer.requestFullscreen) {
            domElements.customPlayer.requestFullscreen();
        }
    });

    // Quality Shifts
    domElements.qualityBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        domElements.qualityDropdown.classList.toggle("hidden");
    });

    // Close Quality flyout
    document.addEventListener("click", () => {
        domElements.qualityDropdown.classList.add("hidden");
    });

    const qualityBtns = domElements.qualityDropdown.querySelectorAll("button");
    qualityBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            qualityBtns.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            
            const qualityAttr = btn.getAttribute("data-quality");
            changeStreamingQuality(qualityAttr);
            domElements.qualityDropdown.classList.add("hidden");
        });
    });

    // Track scrubbing timeline click + drag
    domElements.timelineTrack.addEventListener("mousedown", startScrubTranslate);
    domElements.timelineTrack.addEventListener("touchstart", startScrubTranslate);

    function startScrubTranslate(e) {
        isDraggingTimeline = true;
        document.addEventListener("mousemove", performScrubTranslate);
        document.addEventListener("touchmove", performScrubTranslate);
        document.addEventListener("mouseup", endScrubTranslate);
        document.addEventListener("touchend", endScrubTranslate);
        performScrubTranslate(e);
    }

    function performScrubTranslate(e) {
        const rect = domElements.timelineTrack.getBoundingClientRect();
        const clientX = e.touches ? e.touches[0].clientX : e.clientX;
        let percentage = (clientX - rect.left) / rect.width;
        percentage = Math.max(0, Math.min(1, percentage));

        domElements.timelineProgress.style.width = `${percentage * 100}%`;
        domElements.timelineHandle.style.left = `${percentage * 100}%`;

        if (video.duration) {
            domElements.timeCurrent.textContent = formatTime(percentage * video.duration);
        }
    }

    function endScrubTranslate(e) {
        if (!isDraggingTimeline) return;
        isDraggingTimeline = false;
        
        document.removeEventListener("mousemove", performScrubTranslate);
        document.removeEventListener("touchmove", performScrubTranslate);
        document.removeEventListener("mouseup", endScrubTranslate);
        document.removeEventListener("touchend", endScrubTranslate);

        const rect = domElements.timelineTrack.getBoundingClientRect();
        const eventEnd = e.changedTouches ? e.changedTouches[0] : e;
        let percentage = (eventEnd.clientX - rect.left) / rect.width;
        percentage = Math.max(0, Math.min(1, percentage));

        if (video.duration) {
            video.currentTime = percentage * video.duration;
        }
    }
}

// Immersive Cinema Player core play trigger
function playVideo(movie, episodeObj = null) {
    activePlayingMovie = movie;
    activePlayingEpisode = episodeObj;
    
    domElements.playerMovieTitle.textContent = movie.title;

    if (episodeObj) {
        domElements.playerEpisodeTitle.textContent = `S${episodeObj.seasonNumber} : E${episodeObj.episodeNumber} - ${episodeObj.title}`;
        domElements.playerEpisodeTitle.classList.remove("hidden");
    } else {
        domElements.playerEpisodeTitle.classList.add("hidden");
    }

    // Set streaming url source depending on existing quality (Default 1080p, falls back)
    const streams = episodeObj ? episodeObj.streamUrls : movie.streamUrls;
    let url = streams[activePlayingQuality] || streams["1080p"] || streams["720p"] || streams["360p"];
    
    domElements.playerQualityBtn.textContent = activePlayingQuality;

    domElements.videoElement.src = url;
    domElements.playerLoader.classList.remove("hidden");
    domElements.customPlayer.classList.remove("hidden");

    // Play video
    domElements.videoElement.play()
        .then(() => {
            domElements.playSvg.classList.add("hidden");
            domElements.pauseSvg.classList.remove("hidden");
        })
        .catch(err => {
            console.warn("Autoplay was blocked or failed", err);
        });

    // Browser navigation system backup
    window.history.pushState({ screen: 'player' }, '');
}

function togglePlayPause() {
    const video = domElements.videoElement;
    if (video.paused) {
        video.play();
        domElements.playSvg.classList.add("hidden");
        domElements.pauseSvg.classList.remove("hidden");
    } else {
        video.pause();
        domElements.playSvg.classList.remove("hidden");
        domElements.pauseSvg.classList.add("hidden");
    }
}

// Highly Premium quality shifter (retains playback timestamp!)
function changeStreamingQuality(newQuality) {
    if (newQuality === activePlayingQuality) return;
    activePlayingQuality = newQuality;
    domElements.playerQualityBtn.textContent = newQuality;

    const sourceObject = activePlayingEpisode ? activePlayingEpisode : activePlayingMovie;
    if (!sourceObject) return;

    const currentTimestamp = domElements.videoElement.currentTime;
    const isPaused = domElements.videoElement.paused;

    const streams = sourceObject.streamUrls;
    let newUrl = streams[newQuality] || streams["1080p"] || streams["720p"] || streams["360p"];

    domElements.playerLoader.classList.remove("hidden");
    domElements.videoElement.src = newUrl;
    domElements.videoElement.currentTime = currentTimestamp;

    if (!isPaused) {
        domElements.videoElement.play()
            .then(() => {
                domElements.playSvg.classList.add("hidden");
                domElements.pauseSvg.classList.remove("hidden");
            });
    } else {
        domElements.playSvg.classList.remove("hidden");
        domElements.pauseSvg.classList.add("hidden");
        domElements.playerLoader.classList.add("hidden");
    }

    showToast(`Streaming quality scaled to ${newQuality.toUpperCase()}`);
}

function updatePlayerTimelineDisplays() {
    const video = domElements.videoElement;
    if (!video.duration) return;

    const ratio = (video.currentTime / video.duration);
    domElements.timelineProgress.style.width = `${ratio * 100}%`;
    domElements.timelineHandle.style.left = `${ratio * 100}%`;
    domElements.timeCurrent.textContent = formatTime(video.currentTime);
}

function formatTime(seconds) {
    if (isNaN(seconds)) return "00:00";
    const hrs = Math.floor(seconds / 3600);
    const mins = Math.floor((seconds % 3600) / 60);
    const secs = Math.floor(seconds % 60);

    const pad = (n) => n.toString().padStart(2, '0');

    if (hrs > 0) {
        return `${pad(hrs)}:${pad(mins)}:${pad(secs)}`;
    }
    return `${pad(mins)}:${pad(secs)}`;
}

function closeVideoPlayer(popHistory = true) {
    domElements.videoElement.pause();
    domElements.videoElement.src = "";
    domElements.customPlayer.classList.add("hidden");
    activePlayingMovie = null;
    activePlayingEpisode = null;

    if (popHistory && window.history.state && window.history.state.screen === 'player') {
        window.history.back();
    }
}

domElements.playerCloseBtn.addEventListener("click", () => {
    closeVideoPlayer();
});


// Universal elegant micro toast feedback widget
function showToast(message) {
    const prev = document.querySelector(".cinemana-toast");
    if (prev) prev.remove();

    const toast = document.createElement("div");
    toast.className = "cinemana-toast";
    toast.style.cssText = `
        position: fixed;
        bottom: 85px;
        left: 50%;
        transform: translateX(-50%) translateY(20px);
        background-color: rgba(19, 23, 27, 0.95);
        color: #FFF;
        padding: 10px 18px;
        border-radius: 8px;
        border: 1px solid var(--primary);
        font-size: 11.5px;
        font-weight: 700;
        z-index: 1000;
        opacity: 0;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
        transition: opacity 0.3s, transform 0.3s cubic-bezier(0.18, 0.89, 0.32, 1.28);
        white-space: nowrap;
        pointer-events: none;
    `;
    toast.textContent = message;
    document.body.appendChild(toast);

    // Fade trigger
    setTimeout(() => {
        toast.style.opacity = "1";
        toast.style.transform = "translateX(-50%) translateY(0)";
    }, 10);

    // Expiration
    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateX(-50%) translateY(-10px)";
        setTimeout(() => toast.remove(), 300);
    }, 2800);
}
