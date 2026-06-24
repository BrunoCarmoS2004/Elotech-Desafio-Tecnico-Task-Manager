export interface Project {
  id: string;
  name: string;
  description: string;
  creatorId: string;
}

export interface CreateProjectPayload {
  name: string;
  description: string;
  creatorId: string;
  members: string[];
}

export interface AddMember{
  projectId: string,
  memberIds: string[];
}

export interface AddMemberPayload{
  projectId: string,
  memberIds: string[];
}

export interface ProjectReport{
  byStatus:{
    PENDING: number,
    IN_PROGRESS: number,
    COMPLETED: number
  }
  byPriority:{
    LOW: number,
    MEDIUM: number,
    HIGH: number
  }
}
