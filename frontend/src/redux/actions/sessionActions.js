export const createSession = (session) => ({
    type: 'CREATE_SESSION',
    payload: session,
});

export const addRestaurant = (restaurant) => ({
    type: 'ADD_RESTAURANT',
    payload: restaurant,
});

// TODO
