export interface WebAppointmentUnit {
  unitId: number;
  unitName: string;
}

export interface WebAppointmentSpeciality {
  specialityId: number;
  specialityName: string;
  isClinicalSpeciality: 'Y' | 'N';
}

export interface WebAppointmentDoctor {
  doctorId: number;
  userId: number;
  firstName: string;
  lastName: string;
  specialityId: number;
  specialityName: string;
  doctorImage: string;
}
