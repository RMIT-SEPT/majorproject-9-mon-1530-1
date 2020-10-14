//sample return of what should come from the get endpoint

const sampleReturn = {
  '2020-09-27': [],
  '2020-09-28': [],
  '2020-09-29': [],
  '2020-09-30': [
    {
      startDateTime: '2020-09-30T09:00',
      endDateTime: '2020-09-30T10:00',
      available: false,
    },
    {
      startDateTime: '2020-09-30T10:00',
      endDateTime: '2020-09-30T11:00',
      available: false,
    },
    {
      startDateTime: '2020-09-30T11:00',
      endDateTime: '2020-09-30T12:00',
      available: false,
    },
    {
      startDateTime: '2020-09-30T12:00',
      endDateTime: '2020-09-30T13:00',
      available: true,
    },
    {
      startDateTime: '2020-09-30T13:00',
      endDateTime: '2020-09-30T14:00',
      available: true,
    },
    {
      startDateTime: '2020-09-30T14:00',
      endDateTime: '2020-09-30T15:00',
      available: false,
    },
    {
      startDateTime: '2020-09-30T15:00',
      endDateTime: '2020-09-30T16:00',
      available: false,
    },
    {
      startDateTime: '2020-09-30T16:00',
      endDateTime: '2020-09-30T17:00',
      available: false,
    },
    {
      startDateTime: '2020-09-30T17:00',
      endDateTime: '2020-09-30T18:00',
      available: false,
    },
  ],
  '2020-10-01': [
    {
      startDateTime: '2020-10-01T17:00',
      endDateTime: '2020-10-01T18:00',
      available: false,
    },
    {
      startDateTime: '2020-10-01T18:00',
      endDateTime: '2020-10-01T19:00',
      available: false,
    },
    {
      startDateTime: '2020-10-01T19:00',
      endDateTime: '2020-10-01T20:00',
      available: false,
    },
    {
      startDateTime: '2020-10-01T20:00',
      endDateTime: '2020-10-01T21:00',
      available: false,
    },
    {
      startDateTime: '2020-10-01T21:00',
      endDateTime: '2020-10-01T22:00',
      available: false,
    },
    {
      startDateTime: '2020-10-01T22:00',
      endDateTime: '2020-10-01T23:00',
      available: false,
    },
    {
      startDateTime: '2020-10-01T23:00',
      endDateTime: '2020-10-01T24:00',
      available: false,
    },
  ],
  '2020-10-02': [
    {
      startDateTime: '2020-10-02T09:00',
      endDateTime: '2020-10-02T10:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T10:00',
      endDateTime: '2020-10-02T11:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T11:00',
      endDateTime: '2020-10-02T12:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T12:00',
      endDateTime: '2020-10-02T13:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T13:00',
      endDateTime: '2020-10-02T14:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T14:00',
      endDateTime: '2020-10-02T15:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T15:00',
      endDateTime: '2020-10-02T16:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T16:00',
      endDateTime: '2020-10-02T17:00',
      available: false,
    },
    {
      startDateTime: '2020-10-02T17:00',
      endDateTime: '2020-10-02T18:00',
      available: false,
    },
  ],
  '2020-10-03': [
    {
      startDateTime: '2020-10-03T00:00',
      endDateTime: '2020-10-03T01:00',
      available: true,
    },
    {
      startDateTime: '2020-10-03T01:00',
      endDateTime: '2020-10-03T02:00',
      available: true,
    },
    {
      startDateTime: '2020-10-03T02:00',
      endDateTime: '2020-10-03T03:00',
      available: true,
    },
    {
      startDateTime: '2020-10-03T03:00',
      endDateTime: '2020-10-03T04:00',
      available: true,
    },
    {
      startDateTime: '2020-10-03T04:00',
      endDateTime: '2020-10-03T05:00',
      available: true,
    },
    {
      startDateTime: '2020-10-03T05:00',
      endDateTime: '2020-10-03T06:00',
      available: true,
    },
    {
      startDateTime: '2020-10-03T06:00',
      endDateTime: '2020-10-03T07:00',
      available: true,
    },
    {
      startDateTime: '2020-10-03T07:00',
      endDateTime: '2020-10-03T08:00',
      available: true,
    },
  ],
};

const sampleReturnTwo = {
  '2020-10-15': [
    {
      startDateTime: '2020-10-15T09:00:00',
      endDateTime: '2020-10-15T10:00:00',
      available: true,
    },
    {
      startDateTime: '2020-10-15T10:00:00',
      endDateTime: '2020-10-15T11:00:00',
      available: true,
    },
    {
      startDateTime: '2020-10-15T11:00:00',
      endDateTime: '2020-10-15T12:00:00',
      available: true,
    },
    {
      startDateTime: '2020-10-15T12:00:00',
      endDateTime: '2020-10-15T13:00:00',
      available: true,
    },
    {
      startDateTime: '2020-10-15T13:00:00',
      endDateTime: '2020-10-15T14:00:00',
      available: true,
    },
    {
      startDateTime: '2020-10-15T14:00:00',
      endDateTime: '2020-10-15T15:00:00',
      available: true,
    },
    {
      startDateTime: '2020-10-15T15:00:00',
      endDateTime: '2020-10-15T16:00:00',
      available: true,
    },
    {
      startDateTime: '2020-10-15T16:00:00',
      endDateTime: '2020-10-15T17:00:00',
      available: true,
    },
  ],
  '2020-10-14': [],
  '2020-10-17': [],
  '2020-10-16': [],
  '2020-10-13': [],
  '2020-10-12': [],
};

export { sampleReturn, sampleReturnTwo };
