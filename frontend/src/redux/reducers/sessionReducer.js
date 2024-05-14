const initialState = {
    sessions: [],
    currentSession: null,
    restaurants: [],
};

const sessionReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'CREATE_SESSION':
            return {
                ...state,
                currentSession: action.payload,
                sessions: [...state.sessions, action.payload],
            };
        case 'ADD_RESTAURANT':
            return {
                ...state,
                restaurants: [...state.restaurants, action.payload],
            };
        default:
            return state;
    }
};

export default sessionReducer;
