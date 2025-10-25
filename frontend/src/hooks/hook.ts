import api from "@/hooks/api";
import {
  useMutation,
  useQuery,
  useQueryClient,
  type QueryKey,
} from "@tanstack/react-query";
import type { ApiSuccessResponse, ApiError, ApiResponse } from "@/types/api";

const DEFAULT_STALE_TIME = 5 * 60 * 1000;

type QueryOptions<R = ApiSuccessResponse> = {
  enabled?: boolean;
  staleTime?: number;
  cacheTime?: number;
  refetchInterval?: number;
  select?: (data: ApiSuccessResponse) => R;
  onSuccess?: (data: R) => void;
  onError?: (error: ApiError) => void;
};

type MutationOptions<U = unknown, R = ApiSuccessResponse> = {
  invalidateQueries?: QueryKey[];
  onSuccess?: (data: R, variables: U) => void;
  onError?: (error: ApiError, variables: U) => void;
};

const fetcher = async <T>(endpoint: string): Promise<ApiSuccessResponse> => {
  const response = await api.get<ApiResponse<T>>(endpoint);
  if (!response.data.success) throw response;
  return response.data;
};

export const useApiQuery = <R = ApiSuccessResponse>(
  queryKey: QueryKey,
  endpoint: string,
  options?: QueryOptions<R>
) => {
  return useQuery<ApiSuccessResponse, ApiError, R>({
    queryKey,
    queryFn: () => fetcher(endpoint),
    refetchOnWindowFocus: false,
    staleTime: options?.staleTime ?? DEFAULT_STALE_TIME,
    enabled: options?.enabled,
    select: options?.select,
    retry: 1,
    refetchInterval: options?.refetchInterval,
  });
};

export const useApiMutation = <T, U = unknown, R = ApiSuccessResponse>(
  method: "POST" | "PUT" | "PATCH" | "DELETE",
  endpoint: string | ((data: U) => string),
  options?: MutationOptions<U, R>
) => {
  const queryClient = useQueryClient();

  return useMutation<ApiSuccessResponse, ApiError, U, R>({
    mutationFn: async (data: U) => {
      const resolvedEndpoint =
        typeof endpoint === "function" ? endpoint(data) : endpoint;
      let response;
      switch (method) {
        case "POST":
          response = await api.post<ApiResponse<T>>(resolvedEndpoint, data);
          break;
        case "PUT":
          response = await api.put<ApiResponse<T>>(resolvedEndpoint, data);
          break;
        case "PATCH":
          response = await api.patch<ApiResponse<T>>(resolvedEndpoint, data);
          break;
        case "DELETE":
          response = await api.delete<ApiResponse<T>>(
            resolvedEndpoint,
            data ? { data } : undefined
          );
          break;
        default:
          throw new Error(`Unsupported method: ${method}`);
      }
      if (!response.data.success) throw response;
      return response.data;
    },
    onSuccess: (data, variables) => {
      if (options?.invalidateQueries) {
        options.invalidateQueries.forEach((key) => {
          queryClient.invalidateQueries({ queryKey: key });
        });
      }
      options?.onSuccess?.(data as R, variables);
    },
    onError: (error, variables) => {
      options?.onError?.(error, variables);
    },
  });
};
